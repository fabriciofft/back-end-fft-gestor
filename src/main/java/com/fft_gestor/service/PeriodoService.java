package com.fft_gestor.service;

import com.fft_gestor.dto.response.PeriodoSimplificadoResponseDTO;
import com.fft_gestor.exception.RequestException;
import com.fft_gestor.model.CartaoModel;
import com.fft_gestor.repository.CartaoRepository;
import com.fft_gestor.repository.PeriodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PeriodoService {

    @Autowired
    private PeriodoRepository periodoRepository;

    @Autowired
    private CartaoRepository cartaoRepository;


    public List<PeriodoSimplificadoResponseDTO> buscarPeriodosDeUmCartao(Long codigoCartao){
        CartaoModel cartao = buscarCartaoPorCodigo(codigoCartao);

        return periodoRepository.buscarPeriodosDeUmCartao(cartao.getCodigo());
    }


    //Métodos privados
    private CartaoModel buscarCartaoPorCodigo(Long codigo){
        return cartaoRepository.findByCodigo(codigo)
        .orElseThrow(() -> new RequestException("Cartão inexistente!"));
    }
}
