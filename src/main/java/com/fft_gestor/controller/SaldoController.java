package com.fft_gestor.controller;

import com.fft_gestor.dto.request.DefinirSaldoRequestDTO;
import com.fft_gestor.service.SaldoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/saldo")
public class SaldoController {

    @Autowired
    private SaldoService saldoService;


    @GetMapping("/{codigo}")
    public ResponseEntity<?> buscarSaldoDeUmUSuarioPeloCodigo(@PathVariable Long codigo){
        return new ResponseEntity<>(saldoService.buscarSaldoDeUmUsuarioPeloCodigo(codigo), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> definirSaldoInicial(@RequestBody DefinirSaldoRequestDTO definirSaldoRequestDTO){
        return new ResponseEntity<>(saldoService.definirSaldoInicial(definirSaldoRequestDTO), HttpStatus.OK);
    }

    @PutMapping("/{codigo}")
    public ResponseEntity<?> reiniciarGestaoDeUmUsuario(@PathVariable Long codigo){
        return new ResponseEntity<>(saldoService.reiniciarGestaoDeUmUsuario(codigo), HttpStatus.OK);
    }
}
