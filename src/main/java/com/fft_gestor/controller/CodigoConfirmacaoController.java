package com.fft_gestor.controller;

import com.fft_gestor.service.CodigoConfirmacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/codigoConfirmacao")
public class CodigoConfirmacaoController {

    @Autowired
    private CodigoConfirmacaoService codigoConfirmacaoService;


    @PostMapping("/{email}")
    ResponseEntity<?> gerarEEniviarCodigoDeConfirmacao(@PathVariable String email){
        return new ResponseEntity<>(codigoConfirmacaoService.gerarEEnviarCodigoConfirmacao(email), HttpStatus.CREATED);
    }
}
