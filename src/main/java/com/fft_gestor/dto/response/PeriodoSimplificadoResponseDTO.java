package com.fft_gestor.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PeriodoSimplificadoResponseDTO {

    private Long codigo;
    private Integer diaFechamento;
    private Integer mes;
    private Integer ano;
}
