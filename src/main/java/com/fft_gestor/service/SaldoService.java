package com.fft_gestor.service;

import com.fft_gestor.dto.request.DefinirSaldoRequestDTO;
import com.fft_gestor.dto.response.BuscaLimitesResponseDTO;
import com.fft_gestor.dto.response.SaldoReponseDTO;
import com.fft_gestor.exception.RequestException;
import com.fft_gestor.model.UsuarioModel;
import com.fft_gestor.repository.CartaoRepository;
import com.fft_gestor.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SaldoService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CartaoRepository cartaoRepository;



    public SaldoReponseDTO buscarSaldoDeUmUsuarioPeloCodigo(Long codigo){
        UsuarioModel usuario = buscarUsuarioPorCodigo(codigo);

        BuscaLimitesResponseDTO buscaLimites = cartaoRepository.buscarTotalDeLimiteDeCreditoUtilizadoPorUmUsuario(usuario.getCodigo());

        return new SaldoReponseDTO(
            usuario.getSaldoInicial(),
            usuario.getSaldoAtual(),
            buscaLimites.getLimiteUtilizado(),
            buscaLimites.getLimiteTotal()
        );
    }

    public SaldoReponseDTO definirSaldoInicial(DefinirSaldoRequestDTO definirSaldoRequestDTO){
        UsuarioModel usuario = buscarUsuarioPorCodigo(definirSaldoRequestDTO.getCodigoUsuario());

        usuario.setSaldoInicial(definirSaldoRequestDTO.getSaldoInicial());
        usuario.setSaldoAtual(definirSaldoRequestDTO.getSaldoInicial());

        usuarioRepository.save(usuario);

        return new SaldoReponseDTO(
            usuario.getSaldoInicial(),
            usuario.getSaldoAtual(),
            0.0,
            0.0
        );
    }

    public SaldoReponseDTO reiniciarGestaoDeUmUsuario(Long codigo){
        UsuarioModel usuario = buscarUsuarioPorCodigo(codigo);

        usuario.setSaldoInicial(0.0);
        usuario.setSaldoAtual(0.0);

        usuario.getDespesas().clear();;
        usuario.getDias().clear();
        usuario.getCategorias().clear();
        usuario.getCartoes().clear();

        usuarioRepository.save(usuario);

        return new SaldoReponseDTO(
        0.0,
        0.0,
        0.0,
        0.0
        );
    }

    //Métodos privado
    private UsuarioModel buscarUsuarioPorCodigo(Long codigo){
        return usuarioRepository.findByCodigo(codigo)
                .orElseThrow(() -> new RequestException("Usuário inexitente!"));
    }
}
