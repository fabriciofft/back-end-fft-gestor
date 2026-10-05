package com.fft_gestor.service;

import com.fft_gestor.dto.request.AdicionarAcaoRequestDTO;
import com.fft_gestor.exception.RequestException;
import com.fft_gestor.model.*;
import com.fft_gestor.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CompraService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private DiaRepository diaRepository;

    @Autowired
    private PeriodoRepository periodoRepository;

    @Autowired
    private CompraRepository compraRepository;

    @Autowired
    private CartaoRepository cartaoRepository;

    @Autowired
    private AcaoService acaoService;


    public List<CompraModel> listarComprasDeUmPeriodo(Long codigoPeriodo){
        PeriodoModel periodo = buscarPeriodoPorCodigo(codigoPeriodo);

        return  periodo.getCompras();
    }

    public List<CompraModel> quitarCompras(List<Long> codigos){
        if(codigos.isEmpty()){
            throw new RequestException("Você precisa passar ao menos 1 código!");
        }

        Long primeiroCodigo = codigos.getFirst();
        PeriodoModel periodo = buscarPeriodoPorCodigoDeCompra(primeiroCodigo);

        for(Long codigo: codigos){
            if(buscarCompraPorCodigo(codigo).getQuitado()){
                throw new RequestException("Você só pode passar compras que ainda não foram quitadas!");
            }
            if(!periodo.getCodigo().equals(buscarPeriodoPorCodigoDeCompra(codigo).getCodigo())){
                throw new RequestException("Você só pode passar compras do mesmo periodo!");
            }
        }

        for(Long codigo: codigos){
            quitarCompra(codigo);
        }

        return periodo.getCompras();
    }

    public CompraModel quitarCompra(Long codigo){
        CompraModel compra = buscarCompraPorCodigo(codigo);

        UsuarioModel usuario = buscarUsuarioPorCodigoDeCompra(compra.getCodigo());
        DiaModel diaAtual = buscarDiaAtualPorDataDeUmUsuario(LocalDate.now(), usuario.getCodigo());

        CartaoModel cartao = buscarCartaoPorCodigoDeCompra(compra.getCodigo());

        if(compra.getQuitado()){
            throw new RequestException("Essa compra já foi quitada!");
        }

        compra.setQuitado(true);
        cartao.setLimiteUtilizado(cartao.getLimiteUtilizado() - compra.getValor());

        AdicionarAcaoRequestDTO adicionarAcaoRequestDTO = new AdicionarAcaoRequestDTO(
            diaAtual.getCodigo(),
            cartao.getCodigo(),
            cartao.getApelido(),
            compra.getDescricao(),
            "quitacaoCartao",
            0,
            compra.getValor(),
            compra.getIdCompra()
        );

        acaoService.adicionarAcaoEmUmDia(adicionarAcaoRequestDTO);
        cartaoRepository.save(cartao);
        return  compra;
    }


    //Métodos privados
    private UsuarioModel buscarUsuarioPorCodigoDeCompra(Long codigo){
        return usuarioRepository.buscarUsuarioPorCodigoDeCompra(codigo)
                .orElseThrow(() -> new RequestException("Usuário inexistente!"));
    }

    private DiaModel buscarDiaAtualPorDataDeUmUsuario(LocalDate data, Long codigoUsuario){
        return diaRepository.buscarDiaAtualPorDataDeUmUsuario(data, codigoUsuario)
                .orElseThrow(() -> new RequestException("O dia atual ainda não foi adicionado no menu Gestão. Para você possa quitar essa compra, adicione o dia atual no menu Gestão!"));
    }

    private CartaoModel buscarCartaoPorCodigoDeCompra(Long codigoCompra){
        return  cartaoRepository.buscarCartaoPorCodigoDeCompra(codigoCompra)
                .orElseThrow(() -> new RequestException("Cartão inexistente!"));
    }

    private PeriodoModel buscarPeriodoPorCodigoDeCompra(Long codigoCompra){
        return  periodoRepository.buscarPeriodoPorCodigoDeCompra(codigoCompra)
                .orElseThrow(() -> new RequestException("Periodo inexistente!"));
    }

    private PeriodoModel buscarPeriodoPorCodigo(Long codigo){
        return  periodoRepository.findByCodigo(codigo)
                .orElseThrow(() -> new RequestException("Periodo inexistente!"));
    }

    private CompraModel buscarCompraPorCodigo(Long codigo){
        return  compraRepository.findByCodigo(codigo)
                .orElseThrow(() -> new RequestException("Compra inexistente!"));
    }
}
