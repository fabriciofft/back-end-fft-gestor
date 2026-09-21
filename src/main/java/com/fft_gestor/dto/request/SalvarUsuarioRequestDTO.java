package com.fft_gestor.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SalvarUsuarioRequestDTO {

    private Integer codigoConfirmacao;
    private String nome;
    private String email;
    private String senha;
}
