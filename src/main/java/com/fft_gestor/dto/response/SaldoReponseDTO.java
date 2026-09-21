package com.fft_gestor.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SaldoReponseDTO {

    private Double saldoInicial;
    private Double saldoAtual;
    private Double totalLimiteUtilizado;
    private Double totalLimiteTotal;
}
