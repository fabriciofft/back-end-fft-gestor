package com.fft_gestor.controller;

import com.fft_gestor.dto.request.SalvarDespesaRequestDTO;
import com.fft_gestor.dto.request.LancarDespesaRequestDTO;
import com.fft_gestor.service.DespesaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/despesa")
public class DespesaController {

    @Autowired
    private DespesaService despesaService;


    @GetMapping("/{codigo}")
    public ResponseEntity<?> listarDespesasDeUmUsuario(@PathVariable Long codigo){
        return new ResponseEntity<>(despesaService.listarDespesasDeUmUsuario(codigo), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> salvarDespesaDeUmUsuario(@RequestBody SalvarDespesaRequestDTO salvarDespesaRequestDTO){
        return new ResponseEntity<>(despesaService.salvarDespesaDeUmUsuario(salvarDespesaRequestDTO), HttpStatus.CREATED);
    }

    @PostMapping("/lancarDespesa")
    public ResponseEntity<?> lancarDespesaDeUmUsuario(@RequestBody LancarDespesaRequestDTO lancarDespesaRequestDTO){
        return new ResponseEntity<>(despesaService.lancarDespesaDeUmUsuario(lancarDespesaRequestDTO), HttpStatus.CREATED);
    }

    @DeleteMapping("/{codigo}")
    public ResponseEntity<?> excluirDepesaPorCodigo(@PathVariable Long codigo){
        return new ResponseEntity<>(despesaService.excluirDespesaPorCodigo(codigo), HttpStatus.OK);
    }
}
