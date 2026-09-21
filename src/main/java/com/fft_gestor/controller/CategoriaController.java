package com.fft_gestor.controller;

import com.fft_gestor.dto.request.SalvarCategoriaRequestDTO;
import com.fft_gestor.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/categoria")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;


    @GetMapping(path = "/{codigo}")
    public ResponseEntity<?> listarCategoriasDeUmUsuario(@PathVariable  Long codigo){
        return new ResponseEntity<>(categoriaService.listarCategoriasDeUmUsuario(codigo), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> salvarCategoria(@RequestBody SalvarCategoriaRequestDTO salvarCategoriaRequestDTO){
        return new ResponseEntity<>(categoriaService.salvarCategoria(salvarCategoriaRequestDTO), HttpStatus.CREATED);
    }

    @DeleteMapping("/{codigo}")
    public ResponseEntity<?> excluirCategoria(@PathVariable Long codigo){
        return new ResponseEntity<>(categoriaService.excluirCategoria(codigo), HttpStatus.OK);
    }
}
