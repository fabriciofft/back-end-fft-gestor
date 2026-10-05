package com.fft_gestor.dto.response;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AcaoCategoriaResponseDTO {

    private Long codigo;
    private LocalDate data;
    private String horario;
    private String tipoTransacao;
    private Double valor;
    private String apelidoCartao;
}
