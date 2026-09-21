package com.fft_gestor.controller;

import com.fft_gestor.dto.request.CriarCartaoRequestDTO;
import com.fft_gestor.dto.request.LancarCompraNoCartaoRequestDTO;
import com.fft_gestor.dto.response.CartaoResponseDTO;
import com.fft_gestor.model.CartaoModel;
import com.fft_gestor.service.CartaoService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/cartao")
public class CartaoController {

    @Autowired
    private CartaoService cartaoService;

    @Autowired
    private ModelMapper modelMapper;


    @GetMapping(path = "/cartoesDeUmUsuario/{codigoUsuario}")
    public ResponseEntity<?> listarCartoesDeUmUsuario(@PathVariable Long codigoUsuario){
        return new ResponseEntity<>(converterEmListaDeCartaoResponseDTO(cartaoService.listarCartoesDeUmUsuario(codigoUsuario)), HttpStatus.OK);
    }

    @GetMapping(path = "/{codigo}")
    public ResponseEntity<?> buscarCartaoPorCodigo(@PathVariable Long codigo){
        return new ResponseEntity<>(cartaoService.buscarCartaoPorCodigo(codigo), HttpStatus.OK);
    }

    @GetMapping(path = "/cartaoDeUmUsuario/usuario/{codigoUsuario}/cartao/{codigoCartao}")
    public ResponseEntity<?> buscarCartaoDeUmUsuario(@PathVariable Long codigoUsuario,
                                                     @PathVariable Long codigoCartao){
        return new ResponseEntity<>(cartaoService.buscarCartaoDeumUsuario(codigoUsuario, codigoCartao), HttpStatus.OK);
    }

    @PostMapping(path = "/paraUsuario")
    public ResponseEntity<?> criarCartaoParaUsuario(@RequestBody CriarCartaoRequestDTO criarCartaoRequest){
        return new ResponseEntity<>(cartaoService.criarCartaoParaUsuario(criarCartaoRequest), HttpStatus.CREATED);
    }

    @PostMapping(path = "/lancarCompra")
    public ResponseEntity<?> lancarCompraNoCartao(@RequestBody LancarCompraNoCartaoRequestDTO lancarCompraNoCartaoRequest){
        return new ResponseEntity<>(cartaoService.lancarCompraNoCartao(lancarCompraNoCartaoRequest), HttpStatus.CREATED);
    }

    @DeleteMapping
    public ResponseEntity<?> excluirCartoes(){
        return new ResponseEntity<>(cartaoService.excluirTodosCartoes(), HttpStatus.CREATED);
    }


    //MEtodos privados
    private List<CartaoResponseDTO> converterEmListaDeCartaoResponseDTO(List<CartaoModel> cartoes){
        List<CartaoResponseDTO> listaCartoesRespopnse = new ArrayList<>();

        for(CartaoModel cartao: cartoes){
            listaCartoesRespopnse.add(modelMapper.map(cartao, CartaoResponseDTO.class));
        }

        return listaCartoesRespopnse;
    }
}
