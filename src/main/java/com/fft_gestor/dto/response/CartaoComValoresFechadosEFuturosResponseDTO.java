package com.fft_gestor.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartaoComValoresFechadosEFuturosResponseDTO {

    private Long codigo;
    private String apelido;
    private Integer ultimosDigitos;
    private Double limiteTotal;
    private Double limiteUtilizado;
    private String cor;
    private Integer diaFechamento;
    private Double limiteFechadoUtilizado;
    private Double limiteAtualUtilizado;
    private Double limiteFuturoUtilizado;
}
