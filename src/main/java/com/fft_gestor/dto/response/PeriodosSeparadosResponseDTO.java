package com.fft_gestor.dto.response;

import com.fft_gestor.model.PeriodoModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PeriodosSeparadosResponseDTO {

    List<PeriodoModel> periodosAtuais;
    List<PeriodoModel> periodosPassados;
}
