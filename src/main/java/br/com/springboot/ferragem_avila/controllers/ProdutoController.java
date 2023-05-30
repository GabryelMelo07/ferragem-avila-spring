package br.com.springboot.ferragem_avila.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RestController;

import br.com.springboot.ferragem_avila.model.Produto;
import br.com.springboot.ferragem_avila.repository.ProdutoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RestController
public class ProdutoController {

    @Autowired
    private ProdutoRepository produtoRepository;

    @GetMapping(value = "listartodos_produto") // Método para listar todos objetos do bd \\    
    public ResponseEntity<Iterable<Produto>> listartodos_produto() {
        return new ResponseEntity<>(produtoRepository.list(), HttpStatus.OK);
    }

    @PostMapping(value = "salvar_produto")
    @ResponseBody
    public ResponseEntity<Produto> salvar_produto(@RequestBody Produto produto) {
        Produto prod = produtoRepository.save(produto);
        return new ResponseEntity<Produto>(prod, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "deletar_produto")
    @ResponseBody
    public ResponseEntity<String> deletar_produto(@RequestParam int idProduto) {
        produtoRepository.delete(idProduto);
        return new ResponseEntity<String>("Produto Deletado.", HttpStatus.OK);
    }

    @GetMapping(value = "buscar_produto")
    @ResponseBody
    public ResponseEntity<Produto> buscar_produto(@RequestParam int idProduto) {
        Produto prod = produtoRepository.load(idProduto);
        return new ResponseEntity<Produto>(prod, HttpStatus.OK);
    }

    @GetMapping(value = "buscar_por_nome")
    @ResponseBody
    public ResponseEntity<List<Produto>> buscar_por_nome(@RequestParam String descricao) {
        List<Produto> produtos = produtoRepository.loadByName(descricao);
        return new ResponseEntity<>(produtos, HttpStatus.OK);
    }

    @GetMapping(value = "buscar_por_cod_barras")
    @ResponseBody
    public ResponseEntity<Produto> buscar_por_cod_barras(@RequestParam int cod_barras) {
        Produto prod = produtoRepository.loadByCodBarras(cod_barras);
        return new ResponseEntity<Produto>(prod, HttpStatus.OK);
    }

    @PutMapping(value = "atualizar_produto")
    @ResponseBody
    public ResponseEntity<?> atualizar_produto(@RequestBody Produto produto) {

        if (produto.getId() == 0) {
            return new ResponseEntity<String>("Informe o Id para atualizar o produto.", HttpStatus.OK);
        }

        Produto prod = produtoRepository.update(produto);
        return new ResponseEntity<Produto>(prod, HttpStatus.OK);
    }

    @GetMapping(value = "listar_por_pagina") // Método para listar todos objetos do bd POR PAGINA \\
    public ResponseEntity<Iterable<Produto>> listar_por_pagina(@RequestParam int page) {
        return new ResponseEntity<>(produtoRepository.list_page(page), HttpStatus.OK);
    }

    @GetMapping(value = "num_page") // Método para listar todos objetos do bd POR PAGINA \\
    public int num_page() {
        return produtoRepository.num_pages();
    }
}
