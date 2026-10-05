package com.fft_gestor.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BuscarAcoesPorCategoriaRequestDTO {

    private Long codigoUsuario;
    private String categoria;
    private String periodo;
}
