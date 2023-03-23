package br.com.springboot.ferragem_avila.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RestController;

import br.com.springboot.ferragem_avila.model.Produto;
import br.com.springboot.ferragem_avila.model.Item;
import br.com.springboot.ferragem_avila.repository.ProdutoRepository;
import br.com.springboot.ferragem_avila.repository.ItemRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RestController
public class ItemController {

    @Autowired
    private ProdutoRepository produtoRepository;
    private ItemRepository itemRepository;

    @GetMapping(value = "listartodos_item") // Método para listar todos objetos do bd \\    
    public ResponseEntity<Iterable<Item>> listartodos_item() {
        return new ResponseEntity<>(itemRepository.list(), HttpStatus.OK);
    }

    @PostMapping(value = "salvar_item")
    @ResponseBody
    public ResponseEntity<Item> salvar_item(@RequestBody Item item) {
        Item itemAux = itemRepository.save(item);
        return new ResponseEntity<Item>(itemAux, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "deletar_item")
    @ResponseBody
    public ResponseEntity<String> deletar_item(@RequestParam int idItem) {
        itemRepository.delete(idItem);
        return new ResponseEntity<String>("Item Removido.", HttpStatus.OK);
    }

}