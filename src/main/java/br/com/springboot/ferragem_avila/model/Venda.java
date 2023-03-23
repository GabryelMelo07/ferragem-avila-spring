package br.com.springboot.ferragem_avila.model;

public class Venda {

    private long id;
    private LocalDate data;
    private ArrayList<Item> itens;
    // implementar vendedor

    public Venda(LocalDate data, ArrayList<Item> itens) {
        this.data = data;
        this.itens = itens;
    }

    public long getId() {
        return this.id;
    }

    public LocalDate getData() {
        return this.data;
    }

    public ArrayList<Item> getItens() {
        return this.itens;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public void setIntes(ArrayList<Item> itens) {
        this.itens = itens;
    }

    @Override
    public String toString() {
        return String.format("Venda: %d, %s  -  Itens: %s", this.id, this.data.toString(), this.itens);
    }

}