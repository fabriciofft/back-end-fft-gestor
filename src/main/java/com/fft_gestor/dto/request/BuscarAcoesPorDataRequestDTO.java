package com.fft_gestor.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class BuscarAcoesPorDataRequestDTO {

    private Long codigoUsuario;
    private LocalDate data;
}
