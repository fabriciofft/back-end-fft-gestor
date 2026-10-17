package com.fft_gestor.controller;

import com.fft_gestor.service.PeriodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/periodo")
public class PeriodoController {

    @Autowired
    private PeriodoService periodoService;

    @GetMapping("/periodosDeUmCartao/{codigoCartao}")
    public ResponseEntity<?> buscarPeriodosDeUmCartao(@PathVariable Long codigoCartao){
        return new ResponseEntity<>(periodoService.buscarPeriodosDeUmCartao(codigoCartao), HttpStatus.OK);
    }
}
