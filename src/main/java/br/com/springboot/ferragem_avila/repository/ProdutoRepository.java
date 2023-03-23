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
        String sqlSelect = "SELECT * FROM produto WHERE id = ?;";
        return jdbcTemplate.queryForObject(sqlSelect, BeanPropertyRowMapper.newInstance(Produto.class), id);
    }

}
