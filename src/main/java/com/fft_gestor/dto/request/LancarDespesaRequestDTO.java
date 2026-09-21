package com.fft_gestor.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LancarDespesaRequestDTO {

    private Long codigo;
    private String formaPagamento;
    private Long codigoCartao;
}
