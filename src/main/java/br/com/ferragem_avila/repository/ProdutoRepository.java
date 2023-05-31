package br.com.ferragem_avila.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import br.com.ferragem_avila.model.Produto;

@Repository
public class ProdutoRepository implements IRepository<Produto> {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void delete(int id) {
        String sqlDelete = "DELETE FROM produto where id = ?";
        jdbcTemplate.update(sqlDelete, id);
    }

    @Override
    public Produto update(Produto produto) {
        String sqlUpdate = "UPDATE produto SET descricao = ?, preco = ?, estoque = ?, cod_barras = ? where id = ?";
        jdbcTemplate.update(sqlUpdate, produto.getDescricao(), produto.getPreco(), produto.getEstoque(), produto.getCod_barras(), produto.getId());
        return this.load(produto.getId());
    }

    @Override
    public List<Produto> list() {
        return jdbcTemplate.query("SELECT * FROM produto ORDER BY id ASC", BeanPropertyRowMapper.newInstance(Produto.class));
    }

    public List<Produto> list(int venda_id) {
        return jdbcTemplate.query("SELECT * from produto where produto.id not in (SELECT produto.id from produto inner join item on (produto.id = item.produto_id) where item.venda_id = ?)", BeanPropertyRowMapper.newInstance(Produto.class), venda_id);
    }

    public int num_pages() {
        String selectRows = "SELECT count(*) FROM produto;";
        int rows = jdbcTemplate.queryForObject(selectRows, Integer.class);
        return rows / 2 + rows % 2;  // dividir pela quantidade de itens que quer na página
    }

    public List<Produto> list_page(int page) {
        return jdbcTemplate.query("SELECT * FROM produto ORDER BY id ASC LIMIT 2 OFFSET ?;", BeanPropertyRowMapper.newInstance(Produto.class), ((page - 1) * 2));
    }

    @Override
    public Produto save(Produto produto) {
        String sqlInsert = "INSERT INTO produto (descricao, preco, estoque, cod_barras) VALUES (?,?,?,?) RETURNING id";         
        Integer id = jdbcTemplate.queryForObject(sqlInsert, Integer.class, produto.getDescricao(), produto.getPreco(), produto.getEstoque(), produto.getCod_barras());
        produto.setId(id);
        return produto;
    }

    @Override
    public Produto load(int id) {
        String selectRows = "SELECT count(*) FROM produto WHERE id = ?;";        
        int rows =  jdbcTemplate.queryForObject(selectRows, Integer.class, id);
        if (rows > 0) {        
            String sqlSelect = "SELECT * FROM produto WHERE id = ?;";
            Produto produto = jdbcTemplate.queryForObject(sqlSelect, BeanPropertyRowMapper.newInstance(Produto.class), id);
            return produto;
        } 
        return null;
    }

    public List<Produto> loadByName(String descricao) {
        return jdbcTemplate.query("SELECT * FROM produto WHERE upper(descricao) LIKE '" + descricao.toUpperCase() + "%' ORDER BY id;", BeanPropertyRowMapper.newInstance(Produto.class));
    }

    public Produto loadByCodBarras(long cod_barras) {
        return jdbcTemplate.queryForObject("SELECT * FROM produto WHERE cod_barras = ?;", BeanPropertyRowMapper.newInstance(Produto.class), cod_barras);
    }

}
