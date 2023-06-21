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
        String sqlDelete = "BEGIN; DELETE FROM item WHERE venda_id = ?; DELETE FROM venda where id = ?; COMMIT;";
        jdbcTemplate.update(sqlDelete, id, id);
    }

    @Override
    public Venda update(Venda venda) {
        String sqlUpdate = "UPDATE venda SET concluida = ?, forma_pagamento = ? WHERE id = ?;";
        jdbcTemplate.update(sqlUpdate, venda.getConcluida(), venda.getForma_pagamento(), venda.getId());
        return this.load(venda.getId());
    }

    @Override
    public List<Venda> list() {
        return jdbcTemplate.query("SELECT * FROM venda ORDER BY data_hora DESC", BeanPropertyRowMapper.newInstance(Venda.class));
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

    public List<Venda> listVendaItensByDate(String data) {
        return jdbcTemplate.query("SELECT * FROM venda WHERE CAST(data_hora as date) = '" + data + "' ORDER BY data_hora DESC;", BeanPropertyRowMapper.newInstance(Venda.class));
    }

    public List<Venda> listItensVenda(int id) {
        String sql = "select venda.id as venda, venda.data_hora, venda.forma_pagamento, (select string_agg(item.quantidade || 'x ' || produto.descricao, ', ') as itens from venda inner join item on item.venda_id = venda.id inner join produto on produto.id = item.produto_id where venda.id = ?) from venda where venda.id = ?;";
        return jdbcTemplate.query(sql, new VendaMapper(), id, id);
    }

    public List<Venda> listVendaPorDia(String data) {
        String sql = "select venda.id as venda, venda.data_hora, venda.forma_pagamento, string_agg(item.quantidade || 'x ' || produto.descricao, ', ') as itens from venda inner join item on item.venda_id = venda.id inner join produto on produto.id = item.produto_id where date(venda.data_hora) = '" + data + "' group by venda.id order by venda.data_hora desc;";
        return jdbcTemplate.query(sql, new VendaMapper());
    }

    public List<Venda> listVendaPorMes(String data) {
        String sql = "select venda.id as venda, venda.data_hora, venda.forma_pagamento, string_agg(item.quantidade || 'x ' || produto.descricao, ', ') as itens from venda inner join item on item.venda_id = venda.id inner join produto on produto.id = item.produto_id where TO_CHAR(venda.data_hora, 'YYYY-MM') LIKE '" + data + "' group by venda.id order by venda.data_hora desc;";
        return jdbcTemplate.query(sql, new VendaMapper());
    }

}
