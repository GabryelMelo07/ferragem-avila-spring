/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.springboot.ferragem_avila.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author iapereira
 */
public class ItemMapper  implements RowMapper<Item> {
     //  pendente => de repente deslocar isso para o ItemRepository (como metodo, como uma classe privada/interna ou etc.)
    @Override
    public Item mapRow(ResultSet rs, int rowNum) throws SQLException {
        Item item = new Item();
        item.setId(rs.getInt("id"));
        item.setQuantidade((int)rs.getDouble("quantidade"));
        Produto itemProduto = new Produto();
        itemProduto.setId(rs.getInt("produto_id"));
        itemProduto.setDescricao(rs.getString("descricao"));
        itemProduto.setEstoque(rs.getDouble("estoque"));
        itemProduto.setPreco(rs.getDouble("preco"));
        item.setProduto(itemProduto);
        Venda itemVenda = new Venda();
        itemVenda.setId(rs.getInt("venda_id"));
        itemVenda.setDataHora(rs.getTimestamp("data_hora").toLocalDateTime().toLocalDate());
        item.setVenda(itemVenda);
        return item;
    }
}
