package br.com.ferragem_avila.repository;

import java.util.List;

public interface IRepository<Model> {
    public Model load(int id);
    public void delete(int id);
    public Model update(Model t);
    public Model save(Model t);
    public List<Model> list();    
}