package com.fft_gestor.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "Cartao")
@Table(name = "cartoes")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartaoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codigo;

    @Column(length = 8)
    private String apelido;
    @Column(length = 4)
    private Integer ultimosDigitos;
    private Double limiteTotal;
    private Double limiteUtilizado;

    @Column(length = 10)
    private String cor;

    @OneToMany(
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    @JoinColumn(name = "periodo_id")
    private List<PeriodoModel> periodos = new ArrayList<>();

}
