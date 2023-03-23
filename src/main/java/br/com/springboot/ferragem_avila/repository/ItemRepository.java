package br.com.springboot.ferragem_avila.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import br.com.springboot.ferragem_avila.model.Item;

@Repository
public class ItemRepository implements IRepository<Item> {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Item update(Item item) {
        String sqlUpdate = "UPDATE item SET id = ?, produto = ?, quantidade_produto = ? where id = ?";
        jdbcTemplate.update(sqlUpdate, item.getProduto(), item.getQuantidadeProduto(), item.getId());
        return this.load(item.getId());
    }

    @Override
    public List<Item> list() {
        return jdbcTemplate.query("SELECT * FROM item ORDER BY id ASC", BeanPropertyRowMapper.newInstance(Item.class));
    }

    @Override
    public Item save(Item item) {
        String sqlInsert = "INSERT INTO item (produto, quantidade_produto, venda) VALUES (?,?,?) RETURNING id";         
        Integer id = jdbcTemplate.queryForObject(sqlInsert, Integer.class, item.getProduto(), item.getQuantidadeProduto(), item.getVenda());
        item.setId(id);
        return item;
    }

    @Override
    public Item load(int id) {
        String sqlSelect = "SELECT * FROM item WHERE id = ?;";
        return jdbcTemplate.queryForObject(sqlSelect, BeanPropertyRowMapper.newInstance(Item.class), id);
    }

    @Override
    public void delete(int id) {
        String sqlDelete = "DELETE FROM item where id = ?";
        jdbcTemplate.update(sqlDelete, id);
    }

}