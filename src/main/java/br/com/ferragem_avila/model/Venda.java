package br.com.ferragem_avila.model;

import java.time.LocalDate;
import java.util.ArrayList;

public class Venda {

    private int id;
    private LocalDate data;
    private boolean concluida;
    private ArrayList<Item> itens;
    private String forma_pagamento;
    private String itensString;
    // implementar vendedor

    public Venda(LocalDate data, boolean concluida, ArrayList<Item> itens, String forma_pagamento) {
        this.data = data;
        this.concluida = concluida;
        this.itens = itens;
        this.forma_pagamento = forma_pagamento;
    }

    public Venda() {
        this.itens = new ArrayList<>();
    }

    public int getId() {
        return this.id;
    }

    public LocalDate getData() {
        return this.data;
    }

    public boolean getConcluida() {
        return concluida;
    }

    public ArrayList<Item> getItens() {
        return this.itens;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDataHora(LocalDate data) {
        this.data = data;
    }

    public void setConcluida(boolean concluida) {
        this.concluida = concluida;
    }

    public void setItens(ArrayList<Item> itens) {
        this.itens = itens;
    }

    public String getForma_pagamento() {
        return forma_pagamento;
    }

    public void setForma_pagamento(String forma_pagamento) {
        this.forma_pagamento = forma_pagamento;
    }

    public void setItensString(String string) {
        this.itensString = string;
    }

    public String getItensString() {
        return itensString;
    }

    @Override
    public String toString() {
        return String.format("Venda: %d, %s, %b  -  Itens: %s", this.id, this.data.toString(), this.concluida, this.itens);
    }

}