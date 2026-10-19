package com.fft_gestor.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LancarCompraNoCartaoRequestDTO {

    private Long codigoCartao;
    private LocalDate data;
    private String categoria;
    private Integer indiceIcon;
    private String faturaReferente;
    private Integer numeroParcelas;
    private Double valor;
}
