package br.com.springboot.ferragem_avila.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import br.com.springboot.ferragem_avila.model.Venda;

@Repository
public class VendaRepository implements IRepository<Venda> {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void delete(int id) {
        // TO DO: AJUSTAR MÉTODO PARA NÃO PERMITIR EXCLUIR VENDA FINALIZADA.
        String sqlDelete = "DELETE FROM venda where id = ?";
        jdbcTemplate.update(sqlDelete, id);
    }

    @Override
    public Venda update(Venda venda) {
        String sqlUpdate = "UPDATE venda SET data = ?, itens = ? where id = ?";
        jdbcTemplate.update(sqlUpdate, venda.getData(), venda.getItens(), venda.getId());
        return this.load(venda.getId());
    }

    @Override
    public List<Venda> list() {
        return jdbcTemplate.query("SELECT * FROM venda ORDER BY data ASC", BeanPropertyRowMapper.newInstance(Venda.class));
    }

    @Override
    public Venda save(Venda venda) {
        String sqlInsert = "INSERT INTO venda (data, itens) VALUES (?,?) RETURNING id";         
        Integer id = jdbcTemplate.queryForObject(sqlInsert, Integer.class, venda.getData(), venda.getItens());
        venda.setId(id);
        return venda;
    }

    @Override
    public Venda load(int id) {
        String sqlSelect = "SELECT * FROM venda WHERE id = ?;";
        return jdbcTemplate.queryForObject(sqlSelect, BeanPropertyRowMapper.newInstance(Venda.class), id);
    }
    
}