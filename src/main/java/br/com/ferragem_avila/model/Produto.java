package br.com.ferragem_avila.model;

public class Produto {

    private int id;
    private String descricao;
    private int estoque;
    private double preco;
    private long cod_barras;
    private boolean ativo;
    private String foto;

    public Produto() {
        // precisa ou n precisa....
        // this.foto = null;
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

    public int getEstoque() {
        return estoque;
    }

    public void setEstoque(int estoque) {
        this.estoque = estoque;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public long getCod_barras() {   
        return cod_barras;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public void setCod_barras(long cod_barras) {    
        this.cod_barras = cod_barras;
    }

    public void setFoto(String foto) {
        /*if (foto != null) {
            this.foto = ProdutoController.CAMINHO_IMAGENS + foto;
        } else {
            this.foto = null;
        }*/
        this.foto = foto;
    }

    public String getFoto() {
        return foto;
    }

    @Override
    public String toString() {
        return "Produto{" + "id=" + id + ", descricao=" + descricao + ", estoque=" + estoque + ", preco=" + preco + ", cod_barras=" + cod_barras + "}";  
    }
    
}
