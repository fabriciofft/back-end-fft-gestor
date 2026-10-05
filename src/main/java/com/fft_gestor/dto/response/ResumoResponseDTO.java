package com.fft_gestor.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResumoResponseDTO {

    private List<GastoComCategoriaResponseDTO> gastosPorCategoriasNoSaldoNosUltimosSeteDias;
    private List<GastoComCategoriaResponseDTO> gastosPorCategoriasNoSaldoNosUltimosTrintaDias;
    private List<GastoComCategoriaResponseDTO> gastosPorCategoriasNoCartaoNosUltimosSeteDias;
    private List<GastoComCategoriaResponseDTO> gastosPorCategoriasNoCartaoNosUltimosTrintaDias;
    private List<CartaoSimplificadoResponseDTO> cartoesSimplificados;
}
