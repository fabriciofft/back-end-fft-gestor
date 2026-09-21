package com.fft_gestor.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity(name = "CodigoConfirmacao")
@Table(name = "codigosConfirmacoes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CodigoConfirmacaoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codigo;

    @Column(length = 4)
    private Integer codigoConfirmacao;

    @Column(unique = true, length = 60)
    private String email;

    private LocalDateTime dataExpiracao;
}
