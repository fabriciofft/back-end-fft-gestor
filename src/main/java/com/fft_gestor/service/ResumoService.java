package com.fft_gestor.service;

import com.fft_gestor.dto.response.CartaoSimplificadoResponseDTO;
import com.fft_gestor.dto.response.GastoComCategoriaResponseDTO;
import com.fft_gestor.dto.response.ResumoResponseDTO;
import com.fft_gestor.exception.RequestException;
import com.fft_gestor.model.CartaoModel;
import com.fft_gestor.model.UsuarioModel;
import com.fft_gestor.repository.AcaoRepository;
import com.fft_gestor.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ResumoService {

    @Autowired
    private AcaoRepository acaoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;


    public ResumoResponseDTO buscarGatosPorCategoriaDeUmUsuario(Long codigoUsuario){
        UsuarioModel usuario = buscarUsuarioPorCodigo(codigoUsuario);
        List<CartaoSimplificadoResponseDTO> cartoesSimplificadoUsuario = new ArrayList<>();

        for (CartaoModel cartao: usuario.getCartoes()){
            CartaoSimplificadoResponseDTO cartaoSimplificado = new CartaoSimplificadoResponseDTO(
                cartao.getApelido(),
                cartao.getLimiteUtilizado(),
                cartao.getLimiteTotal()
            );

            cartoesSimplificadoUsuario.add(cartaoSimplificado);
        }

        LocalDate hoje = LocalDate.now();

        return new ResumoResponseDTO(
            acaoRepository.buscarGatosPorCategoriaDeUmUsuario(codigoUsuario, hoje.minusDays(30), hoje),
            cartoesSimplificadoUsuario
        );
    }

    //Métodos privados
    private UsuarioModel buscarUsuarioPorCodigo(Long codigo){
        return usuarioRepository.findByCodigo(codigo)
                .orElseThrow(() -> new RequestException("Usuário inexistente!"));
    }
}
