package com.fft_gestor.service;

import com.fft_gestor.dto.request.BuscarAcoesPorCategoriaRequestDTO;
import com.fft_gestor.dto.request.BuscarAcoesPorDataRequestDTO;
import com.fft_gestor.dto.response.*;
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


    public ResumoResponseDTO buscarGastosPorCategoriaDeUmUsuario(Long codigoUsuario){
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
            buscarGastosUltimos7Dias(codigoUsuario),
            acaoRepository.buscarGastosPorCategoriaNoSaldoDeUmUsuario(codigoUsuario, hoje.minusDays(6), hoje),
            acaoRepository.buscarGastosPorCategoriaNoSaldoDeUmUsuario(codigoUsuario, hoje.minusDays(29), hoje),
            acaoRepository.buscarGastosPorCategoriaNoCartaoDeUmUsuario(codigoUsuario, hoje.minusDays(6), hoje),
            acaoRepository.buscarGastosPorCategoriaNoCartaoDeUmUsuario(codigoUsuario, hoje.minusDays(29), hoje),
            despesaRepository.buscarTotalDespesasDeUmUsuario(codigoUsuario),
            despesaRepository.buscarTotalDespesasLancadasDeUmUsuario(codigoUsuario),
            cartoesSimplificadoUsuario
        );
    }

    public List<AcaoDeUmaDataResponseDTO> buscarAcoesDeUmaDataDeUmUsuario(BuscarAcoesPorDataRequestDTO buscarAcoesPorDataRequest){
        UsuarioModel usuario = buscarUsuarioPorCodigo(buscarAcoesPorDataRequest.getCodigoUsuario());

        return acaoRepository.buscarAcoesDeUmaDataDeumUsuario(usuario.getCodigo(), buscarAcoesPorDataRequest.getData());
    }

    public List<AcaoCategoriaResponseDTO> buscarAcoesPorCategoriaDeumUsuario(BuscarAcoesPorCategoriaRequestDTO buscarAcoesPorCategoriaRequestDTO){
        UsuarioModel usuario = buscarUsuarioPorCodigo(buscarAcoesPorCategoriaRequestDTO.getCodigoUsuario());

        LocalDate hoje = LocalDate.now();
        LocalDate inicio = hoje.minusDays((buscarAcoesPorCategoriaRequestDTO.getPeriodo().equals("semana")) ? 6 : 29);

        return acaoRepository.buscarAcoesPorCategoriaDeumUsuario(usuario.getCodigo(), buscarAcoesPorCategoriaRequestDTO.getCategoria(), inicio, hoje);
    }

    //Métodos privados
    private UsuarioModel buscarUsuarioPorCodigo(Long codigo){
        return usuarioRepository.findByCodigo(codigo)
                .orElseThrow(() -> new RequestException("Usuário inexistente!"));
    }

    private List<GastoPorDiaResponseDTO> buscarGastosUltimos7Dias(Long codigoUsuario){
        UsuarioModel usuario = buscarUsuarioPorCodigo(codigoUsuario);
        LocalDate hoje = LocalDate.now();

        List<GastoPorDiaResponseDTO> gastosUltimos7Dias = new ArrayList<>();

        for(int i = 0; i <= 6; i++){
            LocalDate dia = hoje.minusDays(i);

            GastoResponseDTO gasto = acaoRepository.gastosEmUmaData(usuario.getCodigo(), dia);

            gastosUltimos7Dias.add(
                new GastoPorDiaResponseDTO(
                    dia,
                    gasto.getGastoTotal(),
                    gasto.getGastoComDespesas()
                )
            );
        }

        return gastosUltimos7Dias;
    }
}
