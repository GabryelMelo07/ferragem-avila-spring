package br.com.springboot.ferragem_avila.repository;

import java.util.List;



/**
 *
 * @author iapereira
 * @param <Model>
 */
public interface IRepository<Model> {
    public Model load(int id);
    public void delete(int id);
    public Model update(Model t);
    public Model save(Model t);
    public List<Model> list();    
}