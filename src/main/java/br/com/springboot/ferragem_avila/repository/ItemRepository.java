package br.com.springboot.ferragem_avila.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import br.com.springboot.ferragem_avila.model.Item;
import br.com.springboot.ferragem_avila.model.ItemMapper;

@Repository
public class ItemRepository implements IRepository<Item> {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Item update(Item item) {
        String sqlUpdate = "UPDATE item SET quantidade_produto = ?, preco_item = ?, produto = ? where id = ?";
        Double preco = item.getPreco_item() * item.getQuantidade(); // Testar se funcionou a lógica de alterar o valor do item quando muda a quantidade.
        jdbcTemplate.update(sqlUpdate, item.getQuantidade(), preco, item.getProduto(), item.getId());
        return this.load(item.getId());
    }

    @Override
    public List<Item> list() {
        return jdbcTemplate.query("SELECT * FROM item ORDER BY id ASC", BeanPropertyRowMapper.newInstance(Item.class));
    }
    
    public List<Item> listItensByVenda(int venda_id) {
        String sqlSelectByVenda = "SELECT item.id as id, item.quantidade as quantidade, item.preco_item, item.produto_id, produto.descricao, produto.estoque, produto.cod_barras, produto.preco, item.venda_id, venda.data_hora from item inner join produto on (item.produto_id = produto.id) inner join venda on (venda.id = item.venda_id) WHERE item.venda_id = ?";
        return jdbcTemplate.query(sqlSelectByVenda, new ItemMapper(), venda_id);
    }
    
    @Override
    public Item save(Item item) {
        String sqlInsert = "INSERT INTO item (quantidade, preco_item, produto_id, venda_id) VALUES (?,?,?,?) RETURNING id";      
        Double preco = item.getPreco_item() * item.getQuantidade();   
        Integer id = jdbcTemplate.queryForObject(sqlInsert, Integer.class, item.getQuantidade(), preco, item.getProduto().getId(), item.getVenda().getId());
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

    public void updateQuantidade(int venda_id, int produto_id) {
        String sqlUpdate = "UPDATE item SET quantidade = (quantidade + 1), preco_item = (preco_item * (quantidade + 1))  where venda_id = ? and produto_id = ?;";
        jdbcTemplate.update(sqlUpdate, venda_id, produto_id);
    }
}