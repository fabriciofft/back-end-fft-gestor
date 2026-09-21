package com.fft_gestor.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "MetricasCartao")
@Table(name = "metricasCartoes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MetricasUsoCartaoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codigo;

    private Long codigoCartao;
    @Column(length = 8)
    private String apelidoCartao;
    private Double limiteUsadoInicial;
    private Double limiteUsadoAtual;
    private Double limiteTotal;
}
