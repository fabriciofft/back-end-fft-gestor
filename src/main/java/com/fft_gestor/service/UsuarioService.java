package com.fft_gestor.service;

import com.fft_gestor.dto.request.AlterarSenhaRequestDTO;
import com.fft_gestor.dto.request.FazerLoginRequestDTO;
import com.fft_gestor.dto.request.SalvarUsuarioRequestDTO;
import com.fft_gestor.dto.request.RecuperarSenhaRequestDTO;
import com.fft_gestor.dto.response.LoginResponseDTO;
import com.fft_gestor.exception.RequestException;
import com.fft_gestor.model.CodigoConfirmacaoModel;
import com.fft_gestor.model.UsuarioModel;
import com.fft_gestor.repository.CodigoConfirmacaoRepository;
import com.fft_gestor.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CodigoConfirmacaoRepository codigoConfirmacaoRepository;

    @Autowired
    private PasswordEncoder encoder;


    public List<UsuarioModel> listarUsuarios(){
        return usuarioRepository.findAll();
    }

    public UsuarioModel buscarUsuarioPeloCodigo(Long codigo){
        return buscarUsuarioPorCodigo(codigo);
    }

    public UsuarioModel salvarUsuario(SalvarUsuarioRequestDTO salvarUsuarioRequestDTO){
        if(salvarUsuarioRequestDTO.getNome().split(" ").length <= 1){
            throw new RequestException("Digite o nome completo!!");
        }

        if(usuarioRepository.findByEmail(salvarUsuarioRequestDTO.getEmail().trim()).isPresent()){
            throw new RequestException("Desculpe, este email já esta sendo utilizado!");
        }

        if(salvarUsuarioRequestDTO.getSenha().length() < 8 || salvarUsuarioRequestDTO.getSenha().length() > 20){
            throw new RequestException("A senha deve ter no minímo 8 e no máximo 20 digitos!");
        }

        if(validarCodigoDeConfirmacao(salvarUsuarioRequestDTO.getEmail().trim(), salvarUsuarioRequestDTO.getCodigoConfirmacao())){
            UsuarioModel usuario = new UsuarioModel(
                null,
                salvarUsuarioRequestDTO.getNome(),
                salvarUsuarioRequestDTO.getEmail().trim(),
                encoder.encode(salvarUsuarioRequestDTO.getSenha()),
                0.0,
                0.0,
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>()
            );

            return usuarioRepository.save(usuario);
        }
        else{
            throw new RequestException("Erro ao cadastrar!");
        }
    }

    public LoginResponseDTO fazerLogin(FazerLoginRequestDTO fazerLoginRequestDTO){
        UsuarioModel usuario = buscarUsuarioPorEmail(fazerLoginRequestDTO.getEmail().trim());

        if(encoder.matches(fazerLoginRequestDTO.getSenha(), usuario.getSenha())){
            return new LoginResponseDTO(
                usuario.getCodigo(),
                usuario.getEmail(),
                usuario.getNome(),
                usuario.getSaldoInicial()
            );
        }
        else{
            throw new RequestException("Senha incorreta!");
        }
    }

    public UsuarioModel recuperarSenhaUsuario(RecuperarSenhaRequestDTO recuperarSenhaRequest){
        UsuarioModel usuario = buscarUsuarioPorEmail(recuperarSenhaRequest.getEmail().trim());

        if(recuperarSenhaRequest.getNovaSenha().length() < 8 || recuperarSenhaRequest.getNovaSenha().length() > 20){
            throw new RequestException("A nova senha deve ter no minímo 8 e no máximo 20 digitos!");
        }

        if(validarCodigoDeConfirmacao(recuperarSenhaRequest.getEmail().trim(), recuperarSenhaRequest.getCodigoConfirmacao())){
            usuario.setSenha(encoder.encode(recuperarSenhaRequest.getNovaSenha()));
            return usuarioRepository.save(usuario);
        }
        else{
            throw new RequestException("Erro ao recuperar senha!!");
        }
    }

    public UsuarioModel alterarSenha(AlterarSenhaRequestDTO alterarSenhaRequest){
        UsuarioModel usuario = buscarUsuarioPorCodigo(alterarSenhaRequest.getCodigo());

        if(alterarSenhaRequest.getNovaSenha().length() < 8 || alterarSenhaRequest.getNovaSenha().length() > 20){
            throw new RequestException("A nova senha deve conter no minímo 8 e no máximo 20 digitos!");
        }
        if(!alterarSenhaRequest.getNovaSenha().equals(alterarSenhaRequest.getConfirmacaoNovaSenha())){
            throw new RequestException("A nova senha e sua confirmação devem ser iguais!!");
        }

        if(encoder.matches(alterarSenhaRequest.getSenha(), usuario.getSenha())){
           usuario.setSenha(encoder.encode(alterarSenhaRequest.getNovaSenha()));
           return usuarioRepository.save(usuario);
        }
        else{
            throw new RequestException("Senha atual incorreta!");
        }
    }


    //Métodos privados
    private UsuarioModel buscarUsuarioPorCodigo(Long codigo){
        return usuarioRepository.findByCodigo(codigo)
                .orElseThrow(() -> new RequestException("Usuário inexistente!"));
    }

    private UsuarioModel buscarUsuarioPorEmail(String email){
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RequestException("Usuário inexistente!"));
    }

    private CodigoConfirmacaoModel buscarCodigoConfirmacaoPorEmailDoUsuario(String email){
        return codigoConfirmacaoRepository.findByEmail(email)
                .orElseThrow(() -> new RequestException("Antes de prosseguir solicite o código de confirmação!"));
    }

    private Boolean validarCodigoDeConfirmacao(String email, Integer codigo){
        CodigoConfirmacaoModel codigoConfirmacao = buscarCodigoConfirmacaoPorEmailDoUsuario(email.trim());

        if(codigoConfirmacao.getDataExpiracao().isBefore(LocalDateTime.now())){
            throw new RequestException("O código de recuperação já expirou!");
        }
        else if(codigoConfirmacao.getCodigoConfirmacao().equals(codigo)){
            return true;
        }
        else{
            throw new RequestException("Código de confirmação incorreto!");
        }
    }
}
