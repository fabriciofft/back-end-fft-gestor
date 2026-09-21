package com.fft_gestor.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartaoResponseDTO {

    private Long codigo;
    private String apelido;
    private Integer ultimosDigitos;
    private Double limiteTotal;
    private Double limiteUtilizado;
    private String cor;
}
