package br.com.ferragem_avila.controllers;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.com.ferragem_avila.model.Vendedor;
import br.com.ferragem_avila.repository.VendedorRepository;

@RestController
public class VendedorController {
    
    @Autowired
    private VendedorRepository vendedorRepository;

    public static final String CAMINHO_IMAGENS = System.getProperty("user.dir") + "/src/main/resources/static/img/imagens_perfil/";

    @PostMapping(value = "salvar_vendedor")
    public ResponseEntity<Vendedor> salvar_vendedor(@RequestParam("username") String username, @RequestParam("password") String password, MultipartFile foto) {        
        Vendedor vendedor = new Vendedor();
        // com foto
        if (foto != null && foto.getSize() > 0){
            try {
                String novoNome = UUID.randomUUID().toString()+foto.getOriginalFilename().substring(foto.getOriginalFilename().indexOf("."), foto.getOriginalFilename().length());
                File file = new File(CAMINHO_IMAGENS + novoNome);    
                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(file));
                stream.write(foto.getBytes());
                stream.close();                                
                vendedor.setFoto(novoNome);
            } catch (Exception e) {
                System.err.println("Erro!" + e);
            }
        }
        vendedor.setUsername(username);
        vendedor.setPassword(password);
        return new ResponseEntity<Vendedor>(vendedorRepository.save(vendedor), HttpStatus.CREATED);
    }

    @GetMapping(value = "realizar_login")
    @ResponseBody
    public ResponseEntity<Vendedor> realizar_login(@RequestParam("username") String username, @RequestParam("password") String password) {
        Vendedor vendedor = vendedorRepository.load(username);
        if (vendedor != null) {
            return new ResponseEntity<Vendedor>(vendedor, HttpStatus.OK);
        } else {
            return null;
        }
    }
    
}
