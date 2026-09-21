package com.fft_gestor.service;

import com.fft_gestor.exception.RequestException;
import com.fft_gestor.model.CodigoConfirmacaoModel;
import com.fft_gestor.repository.CodigoConfirmacaoRepository;
import com.fft_gestor.repository.UsuarioRepository;
import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class CodigoConfirmacaoService {

    @Autowired
    private CodigoConfirmacaoRepository codigoConfirmacaoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private final Resend resend;

    @Autowired
    public CodigoConfirmacaoService(@Value("${resend.api-key}") String apiKey) {
        this.resend = new Resend(apiKey);
    }

    public CodigoConfirmacaoModel gerarEEnviarCodigoConfirmacao(String email) {

        CodigoConfirmacaoModel codigoConfirmacao;

        Integer numeroAleatorio = new Random().nextInt(1000, 9999);
        LocalDateTime dataExpiracao = LocalDateTime.now().plusMinutes(10);

        if (codigoConfirmacaoRepository.findByEmail(email).isEmpty()) {
            codigoConfirmacao = new CodigoConfirmacaoModel(
                null,
                numeroAleatorio,
                email,
                dataExpiracao
            );
        } else {
            codigoConfirmacao = codigoConfirmacaoRepository.findByEmail(email).get();

            codigoConfirmacao.setCodigoConfirmacao(numeroAleatorio);
            codigoConfirmacao.setDataExpiracao(dataExpiracao);
        }

        enviarEmailComCodigoDoUsuario(
            email,
            numeroAleatorio,
            "Código de confirmação",
            "cadastro"
        );

        return codigoConfirmacaoRepository.save(codigoConfirmacao);
    }

    private Boolean enviarEmailComCodigoDoUsuario(String email, Integer codigo, String cabecalho, String acao) {

        String texto = "O seu código de " + acao + " é: " + codigo
                     + "\nEle expira em 10 minutos!";

        CreateEmailOptions params = CreateEmailOptions.builder()
        .from("contato@fftflow.dev")
        .to(email)
        .subject(cabecalho)
        .text(texto)
        .build();

        try {
            resend.emails().send(params);
            return true;
        } catch (ResendException e) {
            e.printStackTrace();
            throw new RequestException("Erro ao enviar o código de confirmação para o email informado!");
        }
    }
}
