package br.com.ferragem_avila.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import br.com.ferragem_avila.model.*;

@Repository
public class VendaRepository implements IRepository<Venda> {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void delete(int id) {
        // TO DO: AJUSTAR MÉTODO PARA NÃO PERMITIR EXCLUIR VENDA FINALIZADA.
        String sqlDelete = "BEGIN; DELETE FROM item WHERE venda_id = ?; DELETE FROM venda where id = ?; COMMIT;";
        jdbcTemplate.update(sqlDelete, id, id);
    }

    @Override
    public Venda update(Venda venda) {
        String sqlUpdate = "UPDATE venda SET concluida = ? WHERE id = ?;";
        jdbcTemplate.update(sqlUpdate, venda.getConcluida(), venda.getId());
        return this.load(venda.getId());
    }

    @Override
    public List<Venda> list() {
        return jdbcTemplate.query("SELECT * FROM venda ORDER BY id ASC", BeanPropertyRowMapper.newInstance(Venda.class));
    }

    @Override
    public Venda save(Venda venda) {
        String sqlInsert = "INSERT INTO venda (data_hora) VALUES (CURRENT_TIMESTAMP) RETURNING id";
        venda.setId(jdbcTemplate.queryForObject(sqlInsert, Integer.class));
        return venda;
    }

    @Override
    public Venda load(int id) {
        String sqlSelectRows = "SELECT count(*) as rows FROM venda WHERE id = ?;";
        int rows = jdbcTemplate.queryForObject(sqlSelectRows, Integer.class, id);
        Venda venda = new Venda();
        if (rows > 0) {
            String sqlSelect = "SELECT * FROM venda WHERE id = ?;";
            venda = jdbcTemplate.queryForObject(sqlSelect, BeanPropertyRowMapper.newInstance(Venda.class), id);
        }
        return venda;
    }

    public Item existsItem(int venda_id, int produto_id) {
        String sqlSelectRows = "SELECT count(*) as rows FROM item WHERE venda_id = ? and produto_id = ?;";
        int rows = jdbcTemplate.queryForObject(sqlSelectRows, Integer.class, venda_id, produto_id);
        if (rows > 0) {        
            String sqlSelect = "SELECT * FROM item WHERE venda_id = ? and produto_id = ?;";
            Item item = jdbcTemplate.queryForObject(sqlSelect, BeanPropertyRowMapper.newInstance(Item.class), venda_id, produto_id);
            return item;
        } 
        return null;
    }

}
