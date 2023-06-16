package br.com.ferragem_avila.model;

import java.sql.ResultSet;
import java.sql.SQLException;
// import java.time.LocalDate;

import org.springframework.jdbc.core.RowMapper;

public class VendaMapper implements RowMapper<Venda> {

    @Override
    public Venda mapRow(ResultSet rs, int rowNum) throws SQLException {
        Venda venda = new Venda();
        venda.setId(rs.getInt("venda"));
        venda.setDataHora(rs.getTimestamp("data_hora").toLocalDateTime().toLocalDate());
        venda.setForma_pagamento(rs.getString("forma_pagamento"));
        venda.setItensString(rs.getString("itens"));
        return venda;
    }
    
}
