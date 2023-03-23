package br.com.springboot.ferragem_avila.model;

public class Item {

    private long id;
    private Produto produto;
    private int quantidade_produto;
    private Venda venda;

    public Item item(Produto produto, Venda venda) {
        this.produto = produto;
        this.venda = venda;
    }

    public long getId() {
        return this.id;
    }

    public Produto getProduto() {
        return this.produto;
    }

    public int getQuantidadeProduto() {
        return this.quantidade_produto;
    }

    public Venda getVenda() {
        return this.venda;
    }
    
    public void setId(long id) {
        this.id = id;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public void setQuantidadeProduto(int quantidade_produto) {
        this.quantidade_produto = quantidade_produto;
    }

    public void setVenda(Venda venda) {
        this.venda = venda;
    }

    @Override
    public String toString() {
        return String.format("%d, %s, %d, %s", this.id, this.produto, this.quantidade_produto, this.venda);
    }

}