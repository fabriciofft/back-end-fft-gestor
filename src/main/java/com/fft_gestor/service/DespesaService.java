package com.fft_gestor.service;

import com.fft_gestor.dto.request.AdicionarAcaoRequestDTO;
import com.fft_gestor.dto.request.SalvarDespesaRequestDTO;
import com.fft_gestor.dto.request.LancarDespesaRequestDTO;
import com.fft_gestor.exception.RequestException;
import com.fft_gestor.model.CartaoModel;
import com.fft_gestor.model.DespesaModel;
import com.fft_gestor.model.DiaModel;
import com.fft_gestor.model.UsuarioModel;
import com.fft_gestor.repository.CartaoRepository;
import com.fft_gestor.repository.DespesaRepository;
import com.fft_gestor.repository.DiaRepository;
import com.fft_gestor.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class DespesaService {

    @Autowired
    private DespesaRepository despesaRepository;

    @Autowired
    private  UsuarioRepository usuarioRepository;

    @Autowired
    private DiaRepository diaRepository;

    @Autowired
    private CartaoRepository cartaoRepository;

    @Autowired AcaoService acaoService;


    public List<DespesaModel> listarDespesasDeUmUsuario(Long codigo){
        UsuarioModel usuario = buscarUsuarioPorCodigo(codigo);

        return despesaRepository.listarDespesasDeUmUsuario(usuario.getCodigo());
    }

    public List<DespesaModel> salvarDespesaDeUmUsuario(SalvarDespesaRequestDTO salvarDespesaRequestDTO){
        UsuarioModel usuario = buscarUsuarioPorCodigo(salvarDespesaRequestDTO.getCodigoUsuario());

        if(usuario.getDespesas().size() >= 20){
            throw new RequestException("Você só pode ter no máximo 20 despesas cadastradas!!");
        }

        DespesaModel despesa = new DespesaModel(
            null,
            salvarDespesaRequestDTO.getDescricao(),
            salvarDespesaRequestDTO.getDiaVencimento(),
            salvarDespesaRequestDTO.getValor(),
            false
        );

        usuario.getDespesas().add(despesa);
        usuarioRepository.save(usuario);

        return despesaRepository.listarDespesasDeUmUsuario(usuario.getCodigo());
    }

    public DespesaModel alterarStatusLancamentoDespesa(Long codigo){
        DespesaModel despesa = buscarDespesaPorCodigo(codigo);

        despesa.setJaFoiLancadaEsseMes(!despesa.getJaFoiLancadaEsseMes());

        return despesaRepository.save(despesa);
    }

    public List<DespesaModel> tornarTodasDespesasPendentes(Long codigoUsuario){
        UsuarioModel usuario = buscarUsuarioPorCodigo(codigoUsuario);

        for(DespesaModel despesa: usuario.getDespesas()){
            despesa.setJaFoiLancadaEsseMes(false);
        }

        usuarioRepository.save(usuario);
        return listarDespesasDeUmUsuario(codigoUsuario);
    }

    public List<DespesaModel> excluirDespesaPorCodigo(Long codigo){
       DespesaModel despesa = buscarDespesaPorCodigo(codigo);
       UsuarioModel usuario = buscarUsuarioPorCodigoDeDespesa(despesa.getCodigo());

       usuario.getDespesas().remove(despesa);
       despesaRepository.delete(despesa);

       return usuario.getDespesas();
    }

    public String lancarDespesaDeUmUsuario(LancarDespesaRequestDTO lancarDespesaRequestDTO){
        DiaModel diaAtual = buscarDiaAtualPorData(LocalDate.now());
        CartaoModel cartao = new CartaoModel();
        DespesaModel despesa = buscarDespesaPorCodigo(lancarDespesaRequestDTO.getCodigo());

        Long codigoCartao = null;
        String apelidoCartao = null;

        if(lancarDespesaRequestDTO.getFormaPagamento().equals("cartaoCredito")){
            cartao = buscarCartaoPorCodigo(lancarDespesaRequestDTO.getCodigoCartao());
            codigoCartao = cartao.getCodigo();
            apelidoCartao = cartao.getApelido();
        }

        AdicionarAcaoRequestDTO adicionarAcaoRequest = new AdicionarAcaoRequestDTO(
            diaAtual.getCodigo(),
            codigoCartao,
            apelidoCartao,
            "despesa",
            lancarDespesaRequestDTO.getFormaPagamento(),
        0,
            despesa.getValor(),
            null
        );

        acaoService.adicionarAcaoEmUmDia(adicionarAcaoRequest);

        despesa.setJaFoiLancadaEsseMes(true);
        despesaRepository.save(despesa);

        return  "Lançaca com sucesso!";
    }


    //Métodos privados
    private UsuarioModel buscarUsuarioPorCodigo(Long codigo){
        return usuarioRepository.findByCodigo(codigo)
                .orElseThrow(() -> new RequestException("Usuário inexistente!"));
    }

    private CartaoModel buscarCartaoPorCodigo(Long codigo){
        return cartaoRepository.findByCodigo(codigo)
                .orElseThrow(() -> new RequestException("Cartão inexistente!"));
    }

    private UsuarioModel buscarUsuarioPorCodigoDeDespesa(Long codigoDespesa){
        return usuarioRepository.buscarUsuarioPorCodigoDeDespesa(codigoDespesa)
                .orElseThrow(() -> new RequestException("Usuário inexistente!"));
    }

    private DespesaModel buscarDespesaPorCodigo(Long codigo){
        return despesaRepository.findByCodigo(codigo)
                .orElseThrow(() -> new RequestException("Despesa inexistente!"));
    }

    private DiaModel buscarDiaAtualPorData(LocalDate data){
        return diaRepository.findByData(data)
                .orElseThrow(() -> new RequestException("O dia atual ainda não foi adicionado no menu Gestão. Para você possa quitar essa compra, adicione o dia atual no menu Gestão!"));
    }
}
