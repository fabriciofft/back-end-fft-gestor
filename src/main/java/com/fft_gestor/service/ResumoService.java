package com.fft_gestor.service;

import com.fft_gestor.dto.request.BuscarAcoesPorCategoriaRequestDTO;
import com.fft_gestor.dto.response.AcaoCategoriaResponseDTO;
import com.fft_gestor.dto.response.CartaoSimplificadoResponseDTO;
import com.fft_gestor.dto.response.GastoComCategoriaResponseDTO;
import com.fft_gestor.dto.response.ResumoResponseDTO;
import com.fft_gestor.exception.RequestException;
import com.fft_gestor.model.CartaoModel;
import com.fft_gestor.model.UsuarioModel;
import com.fft_gestor.repository.AcaoRepository;
import com.fft_gestor.repository.DespesaRepository;
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
    private DespesaRepository despesaRepository;

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
            acaoRepository.buscarGastosPorCategoriaNoSaldoDeUmUsuario(codigoUsuario, hoje.minusDays(7), hoje),
            acaoRepository.buscarGastosPorCategoriaNoSaldoDeUmUsuario(codigoUsuario, hoje.minusDays(30), hoje),
            acaoRepository.buscarGastosPorCategoriaNoCartaoDeUmUsuario(codigoUsuario, hoje.minusDays(7), hoje),
            acaoRepository.buscarGastosPorCategoriaNoCartaoDeUmUsuario(codigoUsuario, hoje.minusDays(30), hoje),
            despesaRepository.buscarTotalDespesasDeUmUsuario(codigoUsuario),
            despesaRepository.buscarTotalDespesasLancadasDeUmUsuario(codigoUsuario),
            cartoesSimplificadoUsuario
        );
    }

    public List<AcaoCategoriaResponseDTO> buscarAcoesPorCategoriaDeumUsuario(BuscarAcoesPorCategoriaRequestDTO buscarAcoesPorCategoriaRequestDTO){
        LocalDate hoje = LocalDate.now();
        LocalDate inicio = hoje.minusDays((buscarAcoesPorCategoriaRequestDTO.getPeriodo().equals("semana")) ? 7 : 30);

        return acaoRepository.buscarAcoesPorCategoriaDeumUsuario(buscarAcoesPorCategoriaRequestDTO.getCodigoUsuario(), buscarAcoesPorCategoriaRequestDTO.getCategoria(), inicio, hoje);
    }

    //Métodos privados
    private UsuarioModel buscarUsuarioPorCodigo(Long codigo){
        return usuarioRepository.findByCodigo(codigo)
                .orElseThrow(() -> new RequestException("Usuário inexistente!"));
    }
}
