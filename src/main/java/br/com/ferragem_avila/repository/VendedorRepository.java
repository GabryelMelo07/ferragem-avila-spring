package br.com.ferragem_avila.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import br.com.ferragem_avila.model.Vendedor;

@Repository
public class VendedorRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Vendedor save(Vendedor vendedor) {
        Integer id;
        String sqlInsert;

        if (vendedor.getFoto() == null) {
            sqlInsert = "INSERT INTO vendedor (username, password, foto) VALUES (?,?,'sem_foto.png') RETURNING id";
            id = jdbcTemplate.queryForObject(sqlInsert, Integer.class, vendedor.getUsername(), vendedor.getPassword());
        } else {
            sqlInsert = "INSERT INTO vendedor (username, password, foto) VALUES (?,?,?) RETURNING id";
            id = jdbcTemplate.queryForObject(sqlInsert, Integer.class, vendedor.getUsername(), vendedor.getPassword(), vendedor.getFoto());
        }
        vendedor.setId(id);
        return vendedor;
    }

    public Vendedor load(String username) {
        String sqlSelect = "SELECT * FROM vendedor WHERE username = '" + username + "';";
        return jdbcTemplate.queryForObject(sqlSelect, BeanPropertyRowMapper.newInstance(Vendedor.class));
    }

    public Vendedor loadById(int id) {
        String sqlSelect = "SELECT * FROM vendedor WHERE id = ?;";
        return jdbcTemplate.queryForObject(sqlSelect, BeanPropertyRowMapper.newInstance(Vendedor.class), id);
    }

}
