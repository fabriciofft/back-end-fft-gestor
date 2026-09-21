package com.fft_gestor.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "Periodo")
@Table(name = "periodos")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PeriodoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codigo;

    @Column(length = 2)
    private Integer mes;
    @Column(length = 4)
    private Integer ano;

    @OneToMany(
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    @JoinColumn(name = "compra_id")
    private List<CompraModel> compras = new ArrayList<>();
}
