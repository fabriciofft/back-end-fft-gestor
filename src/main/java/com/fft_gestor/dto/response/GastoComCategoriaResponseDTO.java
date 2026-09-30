package com.fft_gestor.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GastoComCategoriaResponseDTO {

    private String nomeCategoria;
    private Integer indiceIcon;
    private Double valorGastoComSaldo;
    private Double valorGastoComCartao;
}
