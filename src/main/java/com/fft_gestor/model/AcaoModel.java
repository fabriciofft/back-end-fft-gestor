package com.fft_gestor.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "Acao")
@Table(name = "acoes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AcaoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long codigo;
    @Column(length = 10)
    private String categoria;
    @Column(length = 10)
    private String horario;
    @Column(length = 20)
    private String tipoTransacao;
    private Integer indiceIcon;
    private Double valor;
    @Column(length = 8)
    private String apelidoCartao;
    private Long idCompra;
}
