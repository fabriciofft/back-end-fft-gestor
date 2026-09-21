package com.fft_gestor.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdicionarAcaoRequestDTO {

    private Long codigoDia;
    private Long codigoCartao;
    private String apelidoCartao;
    private String categoria;
    private String tipoTransacao;
    private Integer indiceIcon;
    private Double valor;
    private Long idCompra;
}
