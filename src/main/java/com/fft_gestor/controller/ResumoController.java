package com.fft_gestor.controller;

import com.fft_gestor.service.ResumoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/resumo")
public class ResumoController {

    @Autowired
    private ResumoService resumoService;


    @GetMapping(path = "/{codigoUsuario}")
    public ResponseEntity<?> buscarGastosPorCategoriaDeUmUsuario(@PathVariable Long codigoUsuario){
        return new ResponseEntity<>(resumoService.buscarGatosPorCategoriaDeUmUsuario(codigoUsuario), HttpStatus.OK);
    }
}
