package com.fft_gestor.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioResponseDTO {

    private Long codigo;
    private String nome;
    private String email;
    private Double saldoInicial;
    private Double saldoAtual;
}
