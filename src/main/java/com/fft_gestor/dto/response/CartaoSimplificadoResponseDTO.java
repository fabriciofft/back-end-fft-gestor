package com.fft_gestor.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartaoSimplificadoResponseDTO {

    private String apelido;
    private Double limiteUtilizado;
    private Double limiteTotal;
}
