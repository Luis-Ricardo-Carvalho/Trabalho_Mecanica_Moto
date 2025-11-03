package model.data;

import java.util.List;

import model.ModelException;
import model.Motos;

public interface MotosDAO {
	void save(Motos user) throws ModelException;
	void update(Motos user) throws ModelException;
	void delete(Motos user) throws ModelException;
	List<Motos> findAll() throws ModelException;
	List<Motos> findByPecasId(int pecasId) throws ModelException;
    Motos findById(int id) throws ModelException;
}
