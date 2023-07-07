package br.com.ferragem_avila.controllers;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.com.ferragem_avila.model.Produto;
import br.com.ferragem_avila.repository.ProdutoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RestController
public class ProdutoController {

    @Autowired
    private ProdutoRepository produtoRepository;

    // properties
    public static final String CAMINHO_IMAGENS = System.getProperty("user.dir") + "/src/main/resources/static/img/imagens_produtos/";
    
    @GetMapping(value = "listartodos_produto") // Método para listar todos objetos do bd \\    
    public ResponseEntity<Iterable<Produto>> listartodos_produto() {
        return new ResponseEntity<>(produtoRepository.list(), HttpStatus.OK);
    }

    @PostMapping(value = "salvar_produto")
    public ResponseEntity<Produto> salvar_produto(@RequestParam("descricao") String descricao, @RequestParam("preco") double preco, @RequestParam("estoque") int estoque, @RequestParam("cod_barras") long cod_barras, MultipartFile foto) {        
        Produto p = new Produto();
        // com foto
        if (foto != null && foto.getSize() > 0){
            try {
                String novoNome = UUID.randomUUID().toString()+foto.getOriginalFilename().substring(foto.getOriginalFilename().indexOf("."), foto.getOriginalFilename().length());
                File file = new File(CAMINHO_IMAGENS + novoNome);    
                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(file));
                stream.write(foto.getBytes());
                stream.close();                                
                p.setFoto(novoNome);
            } catch (Exception e) {
                System.err.println("Erro!" + e);
            }
        }
        p.setDescricao(descricao);
        p.setPreco(preco);
        p.setCod_barras(cod_barras);
        p.setEstoque(estoque);        
        return new ResponseEntity<Produto>(produtoRepository.save(p), HttpStatus.CREATED);
    }

    @DeleteMapping(value = "deletar_produto")
    @ResponseBody
    public ResponseEntity<String> deletar_produto(@RequestParam int idProduto) {
        Produto p = this.produtoRepository.load(idProduto);        
        // testar o que vem do bd.
        if (p.getFoto() != null && !p.getFoto().equals("sem_imagem.png")){
            File file = new File(CAMINHO_IMAGENS + p.getFoto());                
            file.delete();
        }
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
    public ResponseEntity<Produto> buscar_por_cod_barras(@RequestParam long cod_barras) {
        Produto prod = produtoRepository.loadByCodBarras(cod_barras);
        return new ResponseEntity<Produto>(prod, HttpStatus.OK);
    }

    @PostMapping(value = "atualizar_produto")
    public ResponseEntity<?> atualizar_produto(@RequestParam("id") int id, @RequestParam("descricao") String descricao, @RequestParam("preco") double preco, @RequestParam("estoque") int estoque, @RequestParam("cod_barras") long cod_barras, MultipartFile foto) {
        
        if (id == 0) {
            return new ResponseEntity<String>("Informe o Id para atualizar o produto.", HttpStatus.OK);
        }
        
        Produto p = this.produtoRepository.load(id);        
        if (foto != null && foto.getSize() > 0){       
            if (p.getFoto() != null && !p.getFoto().equals("sem_imagem.png")) {
                File file = new File(CAMINHO_IMAGENS + p.getFoto());                
                file.delete();
                p.setFoto(null);
            }
            try {
                String novoNome = UUID.randomUUID().toString()+foto.getOriginalFilename().substring(foto.getOriginalFilename().indexOf("."), foto.getOriginalFilename().length());
                File file = new File(CAMINHO_IMAGENS + novoNome);    
                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(file));
                stream.write(foto.getBytes());
                stream.close();                                
                p.setFoto(novoNome);
            } catch (Exception e) {
                System.err.println("Erro!" + e);
            }        
        }                
        p.setDescricao(descricao);
        p.setPreco(preco);
        p.setCod_barras(cod_barras);
        p.setEstoque(estoque);  
        produtoRepository.update(p);
        return new ResponseEntity<Produto>(p, HttpStatus.OK);
    }

    @GetMapping(value = "listar_por_pagina") // Método para listar todos objetos do bd POR PAGINA \\
    public ResponseEntity<Iterable<Produto>> listar_por_pagina(@RequestParam int page) {
        return new ResponseEntity<>(produtoRepository.list_page(page), HttpStatus.OK);
    }

    @GetMapping(value = "total_paginas") // Método para listar todos objetos do bd POR PAGINA \\
    public double total_paginas() {
        return produtoRepository.num_pages();
    }
}
