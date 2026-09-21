package com.fft_gestor.controller;

import com.fft_gestor.dto.request.CriarDiaRequestDTO;
import com.fft_gestor.service.DiaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dia")
public class DiaController {

    @Autowired
    private DiaService diaService;

    @GetMapping("/{codigo}")
    public ResponseEntity<?> listarDiasDeUmUsuario(@PathVariable Long codigo){
        return new ResponseEntity<>(diaService.listarDiasDeUmUsuario(codigo), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> criarDia(@RequestBody CriarDiaRequestDTO criarDiaRequestDTO){
        return new ResponseEntity<>(diaService.criarDia(criarDiaRequestDTO), HttpStatus.CREATED);
    }

    @DeleteMapping
    public ResponseEntity<?> excluirDias(){
        return new ResponseEntity<>(diaService.excluirDias(), HttpStatus.OK);
    }

    @DeleteMapping("/{codigo}")
    public ResponseEntity<?> excluirDiaPorCodigo(@PathVariable Long codigo){
        return new ResponseEntity<>(diaService.excluirDiaPeloCodigo(codigo), HttpStatus.OK);
    }
}
