package br.com.ferragem_avila.model;

public class Vendedor {

    private int id;
    private String username;
    private String password;
    private String foto;

    public Vendedor(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Vendedor() {
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getFoto() {
        return foto;
    }
    
    @Override
    public String toString() {
        return this.username;
    }
    
}
