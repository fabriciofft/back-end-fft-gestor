package com.fft_gestor.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "Usuario")
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codigo;

    @Column(length = 80)
    private String nome;
    @Column(unique = true, length = 60)
    private String email;
    @Column(length = 20)
    private String senha;

    private Double saldoInicial;
    private Double saldoAtual;

    @OneToMany(
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    @JoinColumn(name = "dia_id")
    private List<DiaModel> dias = new ArrayList<>();

    @OneToMany(
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    @JoinColumn(name = "categoria_id")
    private List<CategoriaModel> categorias = new ArrayList<>();

    @OneToMany(
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    @JoinColumn(name = "despesa_id")
    private List<DespesaModel> despesas = new ArrayList<>();

    @OneToMany(
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    @JoinColumn(name = "cartao_id")
    private List<CartaoModel> cartoes = new ArrayList<>();
}
