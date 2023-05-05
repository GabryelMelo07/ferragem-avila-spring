package br.com.springboot.ferragem_avila.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import br.com.springboot.ferragem_avila.model.Venda;
import br.com.springboot.ferragem_avila.repository.VendaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    
    @GetMapping(value = "deletar_venda", params = {"venda_id"})
    public ResponseEntity<String> deletar_item(@RequestParam int venda_id) {
        vendaRepository.delete(venda_id);
        return new ResponseEntity<String>("Venda Removida.", HttpStatus.OK);
    }

    @PutMapping(value = "concluir_venda", params = {"id"})
    @ResponseBody
    public ResponseEntity<?> concluir_venda(@RequestParam int id) {
        Venda venda = vendaRepository.load(id);
        venda.setConcluida(true);
        Venda v = vendaRepository.update(venda);
        return new ResponseEntity<Venda>(v, HttpStatus.OK);
    }

    @GetMapping(value = "buscar_venda")
    @ResponseBody
    public ResponseEntity<Venda> buscar_venda(@RequestParam int idVenda) {
        Venda venda = vendaRepository.load(idVenda);
        return new ResponseEntity<Venda>(venda, HttpStatus.OK);
    }

    @GetMapping(value = "listartodos_vendas")
    public ResponseEntity<Iterable<Venda>> listartodos_vendas() {
        return new ResponseEntity<>(vendaRepository.list(), HttpStatus.OK);
    }
    
}
