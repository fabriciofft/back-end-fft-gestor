package com.fft_gestor.controller;

import com.fft_gestor.dto.request.AdicionarAcaoRequestDTO;
import com.fft_gestor.service.AcaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/acao")
public class AcaoController {

    @Autowired
    private AcaoService acaoService;

    @PostMapping
    public ResponseEntity<?> adicionarAcaoEmUmDia(@RequestBody AdicionarAcaoRequestDTO adicionarAcaoRequestDTO){
        return new ResponseEntity<>(acaoService.adicionarAcaoEmUmDia(adicionarAcaoRequestDTO), HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/{codigo}")
    public ResponseEntity<?> reverterAcao(@PathVariable Long codigo){
        return new ResponseEntity<>(acaoService.reverterAcao(codigo), HttpStatus.OK);
    }
}
