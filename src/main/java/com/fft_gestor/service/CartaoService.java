package com.fft_gestor.service;

import com.fft_gestor.dto.request.AlterarLimiteCartaoRequestDTO;
import com.fft_gestor.dto.request.CriarCartaoRequestDTO;
import com.fft_gestor.dto.request.LancarCompraNoCartaoRequestDTO;
import com.fft_gestor.exception.RequestException;
import com.fft_gestor.model.CartaoModel;
import com.fft_gestor.model.CompraModel;
import com.fft_gestor.model.PeriodoModel;
import com.fft_gestor.model.UsuarioModel;
import com.fft_gestor.repository.CartaoRepository;
import com.fft_gestor.repository.CompraRepository;
import com.fft_gestor.repository.PeriodoRepository;
import com.fft_gestor.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class CartaoService {

    @Autowired
    private CartaoRepository cartaoRepository;

    @Autowired
    private PeriodoRepository periodoRepository;

    @Autowired
    private CompraRepository compraRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;


    public List<CartaoModel> listarCartoesDeUmUsuario(Long codigoUsuario){
        UsuarioModel usuario = buscarUsuarioPorCodigo(codigoUsuario);
        return usuario.getCartoes();
    }

    public CartaoModel buscarCartaoPorCodigo(Long codigo){
        return cartaoRepository.findByCodigo(codigo)
                .orElseThrow(() -> new RequestException("Cartão inexistente!"));
    }

    public CartaoModel buscarCartaoDeumUsuario(Long codigoUsuario, Long codigoCartao){
        UsuarioModel usuario = buscarUsuarioPorCodigo(codigoUsuario);
        CartaoModel cartao = buscarCartaoPorCodigo(codigoCartao);

        if(cartaoRepository.buscarCartaoDeUmUsuario(usuario.getCodigo(), cartao.getCodigo()).isPresent()){
            return cartao;
        }
        else{
            throw new RequestException("Você não pode acessar esse cartão!");
        }
    }

    public UsuarioModel criarCartaoParaUsuario(CriarCartaoRequestDTO criarCartaoRequest){
        UsuarioModel usuario = buscarUsuarioPorCodigo(criarCartaoRequest.getCodigoUsuario());

        if(cartaoRepository.buscarCartaoDeUmUsuarioPeloApelido(usuario.getCodigo(), criarCartaoRequest.getApelido().toUpperCase()).isPresent()){
            throw new RequestException("Você já possui um cartão com esse apelido!");
        }

        if(criarCartaoRequest.getApelido().length() > 8){
            throw new RequestException("O apelido só pode conter no máximo 8 caracteres!");
        }

        if(criarCartaoRequest.getDiaFechamento() <= 0 || criarCartaoRequest.getDiaFechamento() > 28){
            throw new RequestException("O dia do vencimento só pode ser entre os dias 1 e 28!!");
        }

        if(usuario.getCartoes().size() >= 5){
            throw new RequestException("Você só pode ter no máximo 5 cartões cadastrados!!");
        }

        CartaoModel cartao = new CartaoModel(
            null,
            criarCartaoRequest.getApelido().toUpperCase(),
            criarCartaoRequest.getUltimosDigitos(),
            criarCartaoRequest.getDiaFechamento(),
            criarCartaoRequest.getLimiteTotal(),
            0.0,
            criarCartaoRequest.getCor(),
            new ArrayList<>()
        );

        usuario.getCartoes().add(cartao);
        usuarioRepository.save(usuario);
        return  usuario;
    }

    public Long lancarCompraNoCartao(LancarCompraNoCartaoRequestDTO lancarCompraNoCartaoRequest){
        CartaoModel cartao = buscarCartaoPorCodigo(lancarCompraNoCartaoRequest.getCodigoCartao());

        if(lancarCompraNoCartaoRequest.getValor() > (cartao.getLimiteTotal() - cartao.getLimiteUtilizado())){
            throw new RequestException("Você não possui limite suficiente nesse cartão para essa compra!");
        }else{
            cartao.setLimiteUtilizado(cartao.getLimiteUtilizado() + lancarCompraNoCartaoRequest.getValor());
        }

        int diaAtual = lancarCompraNoCartaoRequest.getData().getDayOfMonth();
        int mesAtual = lancarCompraNoCartaoRequest.getData().getMonthValue();
        int anoAtual = lancarCompraNoCartaoRequest.getData().getYear();

        Long idCompra = new Random().nextLong(10000, 99999);

        while(compraRepository.findByIdCompra(idCompra).isPresent()){
            idCompra = new Random().nextLong(10000, 99999);
        }

        Integer numeroParcelas = lancarCompraNoCartaoRequest.getNumeroParcelas();

        CompraModel compra = new CompraModel(
            null,
            diaAtual,
            mesAtual,
            lancarCompraNoCartaoRequest.getCategoria(),
            lancarCompraNoCartaoRequest.getValor() / numeroParcelas,
            false,
            idCompra
        );

        if (numeroParcelas < 1) {
            throw new RequestException(
                "O número de parcelas deve ser maior que zero."
            );
        }

        if (diaAtual == cartao.getDiaFechamento()) {
            throw new RequestException(
                "Compras no dia do fechamento não são consideradas."
            );
        }

        YearMonth mesCalendarioAtual =  YearMonth.of(anoAtual, mesAtual);

        // Determina a fatura da primeira parcela
        YearMonth primeiraFatura;

        if (diaAtual < cartao.getDiaFechamento()) {
            primeiraFatura = mesCalendarioAtual.minusMonths(1);
        } else {
            primeiraFatura = mesCalendarioAtual;
        }

        // Cria cada parcela no seu respectivo período
        for (int i = 0; i < numeroParcelas; i++) {

            YearMonth mesDaParcela = primeiraFatura.plusMonths(i);
            String indicadorParcelas = (numeroParcelas > 1) ? " (" + (i + 1)+ "/" + numeroParcelas + ")" : "";

            CompraModel parcela = new CompraModel(
                null,
                compra.getDia(),
                compra.getMes(),
                compra.getDescricao().concat(indicadorParcelas),
                (lancarCompraNoCartaoRequest.getValor() / numeroParcelas),
                false,
                idCompra
            );

            adicionarCompraNoPeriodo(
                cartao,
                parcela,
                mesDaParcela
            );
        }

        cartaoRepository.save(cartao);

        return idCompra;
    }

    public CartaoModel alterarLimiteCartao(AlterarLimiteCartaoRequestDTO alterarLimiteCartaoRequest){
        CartaoModel cartao = buscarCartaoPorCodigo(alterarLimiteCartaoRequest.getCodigoCartao());

        if(alterarLimiteCartaoRequest.getNovoLimite() < cartao.getLimiteUtilizado()){
            throw new RequestException("O novo limite não pode ser menor que o limite utilizado atual. Se deseja diminuir seu limite, primeiro libere o mesmo, quitando compras, na aba 'fatura'!");
        }

        cartao.setLimiteTotal(alterarLimiteCartaoRequest.getNovoLimite());

        return cartaoRepository.save(cartao);
    }

    public CartaoModel limparFaturaCartao(Long codigoCartao){
        CartaoModel cartao = buscarCartaoPorCodigo(codigoCartao);

        cartao.getPeriodos().clear();

        return cartaoRepository.save(cartao);
    }

    public String excluirCartoesDeUmUsuario(Long codigoUsuario){
        UsuarioModel usuario = buscarUsuarioPorCodigo(codigoUsuario);

        usuario.getCartoes().clear();

        usuarioRepository.save(usuario);

        return "Cartões excluidos com sucesso!";
    }


    //Métodos privados
    private UsuarioModel buscarUsuarioPorCodigo(Long codigo){
        return usuarioRepository.findByCodigo(codigo)
                .orElseThrow(() -> new RequestException("Usuário inexistente!"));
    }


    private void adicionarCompraNoPeriodo(
        CartaoModel cartao,
        CompraModel parcela,
        YearMonth mesDaFatura
    ) {
        PeriodoModel periodo;

        Optional<PeriodoModel> periodoExistente =
                periodoRepository.buscarPeriodoEmDeterminadoCartao(
                        cartao.getCodigo(),
                        mesDaFatura.getMonthValue(),
                        mesDaFatura.getYear()
                );

        if (periodoExistente.isEmpty()) {
            System.out.println("Criando período: " + mesDaFatura);

            periodo = new PeriodoModel(
                null,
                mesDaFatura.getMonthValue(),
                mesDaFatura.getYear(),
                new ArrayList<>()
            );

            cartao.getPeriodos().add(periodo);
        } else {
            periodo = periodoExistente.get();
        }

        periodo.getCompras().add(parcela);
    }
}
