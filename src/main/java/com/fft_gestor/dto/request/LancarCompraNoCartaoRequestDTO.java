package com.fft_gestor.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LancarCompraNoCartaoRequestDTO {

    private Long codigoCartao;
    private String categoria;
    private Double valor;
}
