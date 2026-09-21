package com.fft_gestor.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DefinirSaldoRequestDTO {

    private Long codigoUsuario;
    private Double saldoInicial;
}
