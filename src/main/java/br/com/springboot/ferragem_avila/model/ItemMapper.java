package br.com.springboot.ferragem_avila.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class ItemMapper  implements RowMapper<Item> {
    @Override
    public Item mapRow(ResultSet rs, int rowNum) throws SQLException {
        Item item = new Item();
        item.setId(rs.getInt("id"));
        item.setQuantidade(rs.getInt("quantidade"));
        item.setPreco_item(rs.getDouble("preco_item"));
        Produto itemProduto = new Produto();
        itemProduto.setId(rs.getInt("produto_id"));
        itemProduto.setDescricao(rs.getString("descricao"));
        itemProduto.setEstoque(rs.getInt("estoque"));
        itemProduto.setPreco(rs.getDouble("preco"));
        itemProduto.setCod_barras(rs.getLong("cod_barras"));
        item.setProduto(itemProduto);
        Venda itemVenda = new Venda();
        itemVenda.setId(rs.getInt("venda_id"));
        itemVenda.setDataHora(rs.getTimestamp("data_hora").toLocalDateTime().toLocalDate());
        item.setVenda(itemVenda);
        return item;
    }
}
