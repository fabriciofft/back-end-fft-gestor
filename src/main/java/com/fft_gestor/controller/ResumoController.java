package com.fft_gestor.controller;

import com.fft_gestor.dto.request.BuscarAcoesPorCategoriaRequestDTO;
import com.fft_gestor.service.ResumoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/resumo")
public class ResumoController {

    @Autowired
    private ResumoService resumoService;


    @GetMapping(path = "/{codigoUsuario}")
    public ResponseEntity<?> buscarGastosPorCategoriaDeUmUsuario(@PathVariable Long codigoUsuario){
        return new ResponseEntity<>(resumoService.buscarGatosPorCategoriaDeUmUsuario(codigoUsuario), HttpStatus.OK);
    }

    @PostMapping(path = "/acoesPorCategoria")
    public ResponseEntity<?> buscarAcoesPorCategoriaDeUmUsuario(@RequestBody BuscarAcoesPorCategoriaRequestDTO buscarAcoesPorCategoriaRequestDTO){
        return new ResponseEntity<>(resumoService.buscarAcoesPorCategoriaDeumUsuario(buscarAcoesPorCategoriaRequestDTO), HttpStatus.OK);
    }
}
