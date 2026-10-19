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
    private AcaoRepository acaoRepository;

    @Autowired
    private AcaoService acaoService;


    public List<CompraModel> listarComprasDeUmPeriodo(Long codigoPeriodo){
        PeriodoModel periodo = buscarPeriodoPorCodigo(codigoPeriodo);

        return  compraRepository.listarComprasDeUmPeriodoPorOrdemDeData(codigoPeriodo);
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

        UsuarioModel usuario = buscarUsuarioPorCodigoDeCompra(primeiroCodigo);
        CartaoModel cartao = buscarCartaoPorCodigoDeCompra(primeiroCodigo);
        DiaModel diaAtual = buscarDiaAtualPorDataDeUmUsuario(LocalDate.now(), usuario.getCodigo());

        Double valorQuitacao = 0.0;

        for(Long codigo: codigos){
            valorQuitacao += quitarCompra(codigo, cartao);
        }

        AdicionarAcaoRequestDTO adicionarAcaoRequestDTO = new AdicionarAcaoRequestDTO(
            diaAtual.getCodigo(),
            cartao.getCodigo(),
            cartao.getApelido(),
            "quitação cartão" ,
            "quitacaoCartao",
            1,
            "atual",
            1,
            valorQuitacao,
           null
        );

        acaoService.adicionarAcaoEmUmDia(adicionarAcaoRequestDTO);

        return periodo.getCompras();
    }

    public Double quitarCompra(Long codigo, CartaoModel cartao){
        CompraModel compra = buscarCompraPorCodigo(codigo);

        if(compra.getQuitado()){
            throw new RequestException("Essa compra já foi quitada!");
        }

        AcaoModel acao = buscarAcaoPorIdCompra(compra.getIdCompra());
        acao.setTipoTransacao("quitado");

        compra.setQuitado(true);
        cartao.setLimiteUtilizado(cartao.getLimiteUtilizado() - compra.getValor());

        acaoRepository.save(acao);
        cartaoRepository.save(cartao);
        return  compra.getValor();
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

    private AcaoModel buscarAcaoPorIdCompra(long idCompra){
        return acaoRepository.findByIdCompra(idCompra)
                .orElseThrow(() -> new RequestException("Ação inexistente!"));
    }
}
