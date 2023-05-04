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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class ItemController {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private VendaRepository vendaRepository;

    @GetMapping(value = "listar_itens", params = {"venda_id"})   
    public ResponseEntity<Iterable<Item>> listarItens(@RequestParam int venda_id) {
        return new ResponseEntity<>(itemRepository.listItensByVenda(venda_id), HttpStatus.OK);
    }

    @GetMapping(value = "salvar_item", params = {"venda_id", "produto_id", "quantidade"})
    public ResponseEntity<Item> salvar_item(@RequestParam int venda_id, @RequestParam int produto_id, @RequestParam int quantidade) {

        Produto produto = produtoRepository.load(produto_id);

        if (produto != null) {
            Item item = new Item();
            item.setProduto(produto);
//            mudar para uma quantidade correta
            item.setQuantidade(1);

            if (venda_id != 0) {
                // se a venda ja existe venda_id != 0
                item = vendaRepository.existsItem(venda_id, produto_id);
                if (item != null) {
//                  item.setQuantidadeProduto(quantidade);
                    itemRepository.updateQuantidade(venda_id, produto_id);
                    item.setVenda(this.vendaRepository.load(venda_id));
                    return new ResponseEntity<Item>(item, HttpStatus.OK);
                } else {

                    item = new Item();
                    item.setProduto(produto);
                    //            mudar para uma quantidade correta
                    item.setQuantidade(1);

//                    Venda venda = this.vendaRepository.load(venda_id);                     
                    item.setVenda(this.vendaRepository.load(venda_id));
                    itemRepository.save(item);
                    return new ResponseEntity<Item>(item, HttpStatus.OK);
                }
            } else {
                // se n existe ainda venda == venda_id = 0
                Venda venda = new Venda();
                //          ArrayList<Item> vetItem = new ArrayList<>();
//              vetItem.add(item);
                //          venda.setItens(vetItem);
                venda = vendaRepository.save(venda);
                item.setVenda(venda);
                //          item.setVenda(venda);
                itemRepository.save(item);
                return new ResponseEntity<Item>(item, HttpStatus.OK);
            }
        } else {
//            codigo de produto invalido => retorna um item vazio
            return new ResponseEntity<Item>(new Item(), HttpStatus.OK);
        }

    }

    @GetMapping(value = "deletar_item", params = {"item_id"})
    public ResponseEntity<String> deletar_item(@RequestParam int item_id) {
//        testar caso dê errado a exclusão
        itemRepository.delete(item_id);
        return new ResponseEntity<String>("Item Removido.", HttpStatus.OK);
    }

}
