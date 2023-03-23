package br.com.springboot.ferragem_avila.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import br.com.springboot.ferragem_avila.model.Item;

@Repository
public class ItemRepository extends IRepository<Produto, Venda> {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Item update(Item item) {
        String sqlUpdate = "UPDATE item SET id = ?, produto = ?, quantidade_produto = ? where id = ?";
        jdbcTemplate.update(sqlUpdate, item.getDescricao(), item.getPreco(), item.getEstoque(), item.getId());
        return this.load(item.getId());
    }

    @Override
    public List<Produto> list() {
        return jdbcTemplate.query("SELECT * FROM item ORDER BY id ASC", BeanPropertyRowMapper.newInstance(Item.class));
        // add ORDER BY descricao ASC, no select acima.
    }

    @Override
    public Item save(Item item) {
        String sqlInsert = "INSERT INTO item (descricao, preco, estoque) VALUES (?,?,?) RETURNING id";         
        Integer id = jdbcTemplate.queryForObject(sqlInsert, Integer.class, item.getDescricao(), item.getPreco(), item.getEstoque());
        item.setId(id);
        return item;
    }

    @Override
    public Item load(int id) {
        String sqlSelect = "SELECT * FROM item WHERE id = ?;";
        return jdbcTemplate.queryForObject(sqlSelect, BeanPropertyRowMapper.newInstance(Item.class), id);
    }

}