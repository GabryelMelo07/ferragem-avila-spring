package br.com.springboot.ferragem_avila.model;

public class Item  {

    private int id;
    private Produto produto;
    private int quantidade;
    private Venda venda;

    public Item(Produto produto, Venda venda) {
        this.produto = produto;
        this.venda = venda;
    }

    public Item() {
//        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getId() {
        return this.id;
    }

    public Produto getProduto() {
        return this.produto;
    }

    public int getQuantidade() {
        return this.quantidade;
    }

    public Venda getVenda() {
        return this.venda;
    }
    
    public void setId(int id) {
        this.id = id;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public void setVenda(Venda venda) {
        this.venda = venda;
    }

    @Override
    public String toString() {
        return String.format("%d, %s, %d, %s", this.id, this.produto, this.quantidade, this.venda);
    }
    
   

}