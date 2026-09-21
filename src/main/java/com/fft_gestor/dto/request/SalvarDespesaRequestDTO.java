package com.fft_gestor.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SalvarDespesaRequestDTO {

    private Long codigoUsuario;
    private String descricao;
    private Integer diaVencimento;
    private Double valor;
}
