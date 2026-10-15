package com.fft_gestor.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AlterarLimiteCartaoRequestDTO {

    private Long codigoCartao;
    private Double novoLimite;
}
