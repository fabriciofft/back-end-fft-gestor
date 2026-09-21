package com.fft_gestor.controller;

import com.fft_gestor.dto.request.AlterarSenhaRequestDTO;
import com.fft_gestor.dto.request.FazerLoginRequestDTO;
import com.fft_gestor.dto.request.SalvarUsuarioRequestDTO;
import com.fft_gestor.dto.request.RecuperarSenhaRequestDTO;
import com.fft_gestor.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;


    @GetMapping
    public ResponseEntity<?> listarUSuarios(){
        return new ResponseEntity<>(usuarioService.listarUsuarios(), HttpStatus.OK);
    }

    @GetMapping(path = "/{codigo}")
    public ResponseEntity<?> buscarUsuarioPeloCodigo(@PathVariable Long codigo){
        return new ResponseEntity<>(usuarioService.buscarUsuarioPeloCodigo(codigo), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> salvarUsuario(@RequestBody SalvarUsuarioRequestDTO salvarUsuarioRequestDTO){
        return new ResponseEntity<>(usuarioService.salvarUsuario(salvarUsuarioRequestDTO), HttpStatus.CREATED);
    }

    @PostMapping(path = "/login")
    public ResponseEntity<?> fazerLogin(@RequestBody FazerLoginRequestDTO fazerLoginRequestDTO){
        return new ResponseEntity<>(usuarioService.fazerLogin(fazerLoginRequestDTO), HttpStatus.OK);
    }

    @PutMapping(path = "/recuperarSenha")
    public ResponseEntity<?> recuperarSenha(@RequestBody RecuperarSenhaRequestDTO recuperarSenhaRequest){
        return new ResponseEntity<>(usuarioService.recuperarSenhaUsuario(recuperarSenhaRequest), HttpStatus.OK);
    }

    @PutMapping(path = "/alterarSenha")
    public ResponseEntity<?> alterarSenha(@RequestBody AlterarSenhaRequestDTO alterarSenhaRequest){
        return new ResponseEntity<>(usuarioService.alterarSenha(alterarSenhaRequest), HttpStatus.OK);
    }
}
