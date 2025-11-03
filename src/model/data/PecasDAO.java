package model.data;

import java.util.List;

import model.ModelException;
import model.Pecas;

public interface PecasDAO {
	void save(Pecas user) throws ModelException;
	void update(Pecas user) throws ModelException;
	void delete(Pecas user) throws ModelException;
	List<Pecas> findAll() throws ModelException;
	List<Pecas> findByUserId(int userId) throws ModelException;
    Pecas findById(int id) throws ModelException;
}
