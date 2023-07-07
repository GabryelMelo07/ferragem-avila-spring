package br.com.ferragem_avila.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import br.com.ferragem_avila.model.Produto;

@Repository
public class ProdutoRepository implements IRepository<Produto> {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void delete(int id) {
        if (list_sold_products(id).isEmpty()) {
            String sqlDelete = "DELETE FROM produto where id = ?";
            jdbcTemplate.update(sqlDelete, id);
        } else {
            setInativo(id);
        }
    }

    @Override
    public Produto update(Produto produto) { // ADICIONAR FOTO PRODUTO AQUI
        // sem foto
        if(produto.getFoto() == null || produto.getFoto().equals("sem_imagem.png")) {
            String sqlUpdate = "UPDATE produto SET descricao = ?, preco = ?, estoque = ?, cod_barras = ? where id = ?";
            jdbcTemplate.update(sqlUpdate, produto.getDescricao(), produto.getPreco(), produto.getEstoque(), produto.getCod_barras(), produto.getId());
        } else {
            // com nova foto / mantem a atual
            String sqlUpdate = "UPDATE produto SET descricao = ?, preco = ?, estoque = ?, cod_barras = ?, foto = ? where id = ?";
            jdbcTemplate.update(sqlUpdate, produto.getDescricao(), produto.getPreco(), produto.getEstoque(), produto.getCod_barras(), produto.getFoto(), produto.getId());
        }
        return this.load(produto.getId());
    }

    public Produto setInativo(int produto_id) {
        String sqlUpdate = "UPDATE produto SET ativo = false where id = ?";
        jdbcTemplate.update(sqlUpdate, produto_id);
        return this.load(produto_id);
    }

    @Override
    public List<Produto> list() {
        return jdbcTemplate.query("SELECT * FROM produto where ativo = true ORDER BY id ASC", BeanPropertyRowMapper.newInstance(Produto.class));
    }

    public List<Produto> list_sold_products(int produto_id) {
        return jdbcTemplate.query("SELECT * from produto where produto.id in (SELECT produto.id from produto inner join item on (produto.id = item.produto_id) where produto.id = ? and produto.ativo = true);", BeanPropertyRowMapper.newInstance(Produto.class), produto_id);
    }

    public double num_pages() {
        String selectRows = "SELECT count(*) FROM produto where ativo = true;";
        double rows = jdbcTemplate.queryForObject(selectRows, Double.class);
        return Math.ceil(rows / 15.0); // dividir pela quantidade de itens que quer na página
    }

    public List<Produto> list_page(int page) {
        return jdbcTemplate.query("SELECT * FROM produto where ativo = true ORDER BY id ASC LIMIT 15 OFFSET ?;", BeanPropertyRowMapper.newInstance(Produto.class), ((page - 1) * 15));
    }

    @Override
    public Produto save(Produto produto) {
        Integer id;
        String sqlInsert;
        if (produto.getFoto() == null){
            sqlInsert = "INSERT INTO produto (descricao, preco, estoque, cod_barras, foto) VALUES (?,?,?,?, 'sem_foto.png') RETURNING id";         
            id = jdbcTemplate.queryForObject(sqlInsert, Integer.class, produto.getDescricao(), produto.getPreco(), produto.getEstoque(), produto.getCod_barras());            
        } else {
            sqlInsert = "INSERT INTO produto (descricao, preco, estoque, cod_barras, foto) VALUES (?,?,?,?,?) RETURNING id";         
            id = jdbcTemplate.queryForObject(sqlInsert, Integer.class, produto.getDescricao(), produto.getPreco(), produto.getEstoque(), produto.getCod_barras(), produto.getFoto());            
        }
        produto.setId(id);
        return produto;
    }

    @Override
    public Produto load(int id) {
        String selectRows = "SELECT count(*) FROM produto WHERE id = ? and ativo = true;";        
        int rows =  jdbcTemplate.queryForObject(selectRows, Integer.class, id);
        if (rows > 0) {        
            String sqlSelect = "SELECT * FROM produto WHERE id = ? and ativo = true;";
            Produto produto = jdbcTemplate.queryForObject(sqlSelect, BeanPropertyRowMapper.newInstance(Produto.class), id);
            return produto;
        } 
        return null;
    }

    public List<Produto> loadByName(String descricao) {
        return jdbcTemplate.query("SELECT * FROM produto WHERE upper(descricao) LIKE '" + descricao.toUpperCase() + "%' and ativo = true ORDER BY id;", BeanPropertyRowMapper.newInstance(Produto.class));
    }

    public Produto loadByCodBarras(long cod_barras) {
        return jdbcTemplate.queryForObject("SELECT * FROM produto WHERE cod_barras = ? and ativo = true;", BeanPropertyRowMapper.newInstance(Produto.class), cod_barras);
    }

}
