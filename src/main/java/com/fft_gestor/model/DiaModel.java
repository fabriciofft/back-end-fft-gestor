package com.fft_gestor.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "Dia")
@Table(name = "dias")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DiaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codigo;

    private LocalDate data;
    private Double saldoInicial;
    private Double saldoAtual;

    @OneToMany(
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    @JoinColumn(name = "acoes_id")
    private List<AcaoModel> acoes = new ArrayList<>();

    @OneToMany(
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    @JoinColumn(name = "historico_uso_cartao_id")
    private List<MetricasUsoCartaoModel> historicoUsoCartoes = new ArrayList<>();
}
