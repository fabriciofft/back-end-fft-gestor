package com.fft_gestor.controller;

import com.fft_gestor.service.CompraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/compra")
public class CompraController {

    @Autowired
    private CompraService compraService;


    @GetMapping(path = "/periodo/{codigoPeriodo}")
    public ResponseEntity<?> listarComprasDeUmPeriodo(@PathVariable Long codigoPeriodo){
        return new ResponseEntity<>(compraService.listarComprasDeUmPeriodo(codigoPeriodo), HttpStatus.OK);
    }

    @PutMapping(path = "/quitar/{codigo}")
    public ResponseEntity<?> quitarCompra(@PathVariable Long codigo){
        return new ResponseEntity<>(compraService.quitarCompra(codigo), HttpStatus.OK);
    }
}