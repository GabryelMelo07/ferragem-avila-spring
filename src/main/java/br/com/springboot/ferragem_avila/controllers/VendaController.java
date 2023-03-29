package br.com.springboot.ferragem_avila.controllers;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RestController;

import br.com.springboot.ferragem_avila.model.Produto;
import br.com.springboot.ferragem_avila.model.Venda;
import br.com.springboot.ferragem_avila.repository.ProdutoRepository;
import br.com.springboot.ferragem_avila.repository.VendaRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RestController
public class VendaController {

    @Autowired
    private VendaRepository vendaRepository;

    @PostMapping(value = "salvar_venda")
    @ResponseBody
    public ResponseEntity<Venda> salvar_venda(@RequestBody Venda venda) {
        Venda vendaAux = vendaRepository.save(venda);
        return new ResponseEntity<Venda>(vendaAux, HttpStatus.CREATED);
    }
    
}
