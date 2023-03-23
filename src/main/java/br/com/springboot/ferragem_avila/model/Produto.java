package br.com.springboot.ferragem_avila.model;

public class Produto /*implements RowMapper<Produto>*/ {

    private int id;
    private String descricao;
    private double estoque;
    private double preco;

    public Produto() {
    }

    public Produto(String descricao) {
        this.descricao = descricao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getEstoque() {
        return estoque;
    }

    public void setEstoque(double estoque) {
        this.estoque = estoque;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    @Override
    public String toString() {
        return "Produto{" + "id=" + id + ", descricao=" + descricao + ", estoque=" + estoque + ", preco=" + preco + '}';
    }

}
