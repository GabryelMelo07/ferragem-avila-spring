package br.com.springboot.ferragem_avila.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import br.com.springboot.ferragem_avila.model.Produto;

@Repository
public class ProdutoRepository implements IRepository<Produto> {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void delete(int id) {
        String sqlDelete = "DELETE FROM produto where id = ?";
        jdbcTemplate.update(sqlDelete, id);
    }

    @Override
    public Produto update(Produto produto) {
        String sqlUpdate = "UPDATE produto SET descricao = ?, preco = ?, estoque = ? where id = ?";
        jdbcTemplate.update(sqlUpdate, produto.getDescricao(), produto.getPreco(), produto.getEstoque(), produto.getId());
        return this.load(produto.getId());
    }

    @Override
    public List<Produto> list() {
        return jdbcTemplate.query("SELECT * FROM produto ORDER BY descricao ASC", BeanPropertyRowMapper.newInstance(Produto.class));
    }

    public List<Produto> list(int venda_id) {
        return jdbcTemplate.query("SELECT * from produto where produto.id not in (SELECT produto.id from produto inner join item on (produto.id = item.produto_id) where item.venda_id = ?)", BeanPropertyRowMapper.newInstance(Produto.class), venda_id);
    }

    @Override
    public Produto save(Produto produto) {
        String sqlInsert = "INSERT INTO produto (descricao, preco, estoque) VALUES (?,?,?) RETURNING id";         
        Integer id = jdbcTemplate.queryForObject(sqlInsert, Integer.class, produto.getDescricao(), produto.getPreco(), produto.getEstoque());
        produto.setId(id);
        return produto;
    }

    @Override
    public Produto load(int id) {
        String selectRows = "SELECT count(*) FROM produto WHERE id = ?;";        
        int rows =  jdbcTemplate.queryForObject(selectRows, Integer.class, id);
        if (rows > 0) {        
            String sqlSelect = "SELECT * FROM produto WHERE id = ?;";
            Produto produto = jdbcTemplate.queryForObject(sqlSelect, BeanPropertyRowMapper.newInstance(Produto.class), id);
            return produto;
        } 
        return null;
    }
    
//      public Item existsItem(int venda_id, int produto_id) {
//        String sqlSelectRows = "SELECT count(*) as rows FROM item WHERE venda_id = ? and produto_id = ?;";
//        int rows = jdbcTemplate.queryForObject(sqlSelectRows, Integer.class, venda_id, produto_id);
//        if (rows > 0) {        
//            String sqlSelect = "SELECT * FROM item WHERE venda_id = ? and produto_id = ?;";
//            Item item = jdbcTemplate.queryForObject(sqlSelect, BeanPropertyRowMapper.newInstance(Item.class), venda_id, produto_id);
//            return item;
//        } 
//        return null;
//    }

}
