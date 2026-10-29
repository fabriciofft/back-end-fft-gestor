package com.fft_gestor.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AcaoDeUmaDataResponseDTO {

    private Long codigo;
    private String categoria;
    private String apelidoCartao;
    private String horario;
    private Double valor;
}
