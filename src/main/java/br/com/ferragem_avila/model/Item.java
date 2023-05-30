package br.com.ferragem_avila.model;

public class Item  {

    private int id;
    private int quantidade;
    private Double preco_item;
    private Produto produto;
    private Venda venda;

    public Item(Double preco_item, Produto produto, Venda venda) {
        this.preco_item = preco_item;
        this.produto = produto;
        this.venda = venda;
    }

    public Item() {
    }

    public int getId() {
        return this.id;
    }

    public int getQuantidade() {
        return this.quantidade;
    }

    public Double getPreco_item() {
        return preco_item;
    }

    public Produto getProduto() {
        return this.produto;
    }

    public Venda getVenda() {
        return this.venda;
    }
    
    public void setId(int id) {
        this.id = id;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public void setPreco_item(Double preco_item) {
        this.preco_item = preco_item;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public void setVenda(Venda venda) {
        this.venda = venda;
    }

    @Override
    public String toString() {
        return String.format("%d, %d, %f, %s, %s", this.id, this.quantidade, this.preco_item, this.produto, this.venda);
    }

}