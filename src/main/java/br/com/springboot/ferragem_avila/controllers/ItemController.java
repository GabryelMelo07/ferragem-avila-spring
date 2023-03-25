package br.com.springboot.ferragem_avila.controllers;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RestController;

import br.com.springboot.ferragem_avila.model.Item;
import br.com.springboot.ferragem_avila.model.Produto;
import br.com.springboot.ferragem_avila.model.Venda;
import br.com.springboot.ferragem_avila.repository.ItemRepository;
import br.com.springboot.ferragem_avila.repository.ProdutoRepository;
import br.com.springboot.ferragem_avila.repository.VendaRepository;
import java.util.ArrayList;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RestController
public class ItemController {

    @Autowired
    private ItemRepository itemRepository;
    

    @Autowired
    private ProdutoRepository produtoRepository;
    
    @Autowired
    private VendaRepository vendaRepository;

    @GetMapping(value = "listartodos_item") // Método para listar todos objetos do bd \\    
    public ResponseEntity<Iterable<Item>> listartodos_item() {
        return new ResponseEntity<>(itemRepository.list(), HttpStatus.OK);
    }

    @GetMapping(value = "salvar_item", params = {"venda_id", "produto_id", "quantidade_produto"})
    @ResponseBody
    public ResponseEntity<Item> salvar_item(@RequestParam int venda_id, @RequestParam int produto_id, @RequestParam int quantidade_produto) {        
        Produto produto = produtoRepository.load(produto_id);
        Item item = new Item();
        item.setProduto(produto);
        item.setQuantidadeProduto(quantidade_produto);
        if (venda_id > 0){
            // se a venda ja existe venda_id != 0
            item.setVenda(vendaRepository.load(venda_id));
        } else {
            // se n existe ainda venda == venda_id = 0
            Venda venda = new Venda();
            ArrayList<Item> vetItem = new ArrayList<>();
            vetItem.add(item);
            venda.setIntes(vetItem);            
            item.setVenda(vendaRepository.save(venda));
            item.setVenda(venda);
        }
        return new ResponseEntity<Item>(itemRepository.save(item), HttpStatus.OK);
    }

    @DeleteMapping(value = "deletar_item")
    @ResponseBody
    public ResponseEntity<String> deletar_item(@RequestParam int idItem) {
        itemRepository.delete(idItem);
        return new ResponseEntity<String>("Item Removido.", HttpStatus.OK);
    }

}