package com.fft_gestor.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CriarCartaoRequestDTO {

    private Long codigoUsuario;
    private String apelido;
    private Integer ultimosDigitos;
    private Double limiteTotal;
    private String cor;
}
