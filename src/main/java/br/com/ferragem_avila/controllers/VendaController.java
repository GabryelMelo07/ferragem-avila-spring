package br.com.ferragem_avila.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.ferragem_avila.model.Venda;
import br.com.ferragem_avila.repository.VendaRepository;
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
    public ResponseEntity<?> concluir_venda(@RequestParam int id, @RequestParam String forma_pagamento) {
        Venda venda = vendaRepository.load(id);
        venda.setConcluida(true);
        venda.setForma_pagamento(forma_pagamento);
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

    @GetMapping(value = "listar_por_data", params = {"data"})
    public ResponseEntity<Iterable<Venda>> listar_por_data(@RequestParam String data) {
        return new ResponseEntity<>(vendaRepository.listVendaItensByDate(data), HttpStatus.OK);
    }

    @GetMapping(value = "listItensVenda", params = {"id"})
    public ResponseEntity<Iterable<Venda>> listItensVenda(@RequestParam int id) {
        return new ResponseEntity<Iterable<Venda>>(vendaRepository.listItensVenda(id), HttpStatus.OK);
    }

    @GetMapping(value = "listarVendasPorDia", params = {"data"})
    public ResponseEntity<Iterable<Venda>> listarVendasPorDia(@RequestParam String data) {
        return new ResponseEntity<>(vendaRepository.listVendaPorDia(data), HttpStatus.OK);
    }

    @GetMapping(value = "listarVendasPorMes", params = {"ano", "mes"})
    public ResponseEntity<Iterable<Venda>> listarVendasPorMes(@RequestParam String ano, @RequestParam String mes) {
        return new ResponseEntity<>(vendaRepository.listVendaPorMes(ano, mes), HttpStatus.OK);
    }

}
