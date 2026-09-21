package com.fft_gestor.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AlterarSenhaRequestDTO {

    private Long codigo;
    private String senha;
    private String novaSenha;
    private String confirmacaoNovaSenha;
}
