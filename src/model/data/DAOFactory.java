package model.data;

import model.data.mysql.MySQLMotosDAO;
import model.data.mysql.MySQLPecasDAO;
import model.data.mysql.MySQLUserDAO;

public final class DAOFactory {	

	public static PecasDAO createPecasDAO() {
		return new MySQLPecasDAO();
	}
	
	public static UserDAO createUserNDAO() {
		return new MySQLUserDAO();
	}
	
	public static MotosDAO createMotosDAO() {
		return new MySQLMotosDAO();
	}
}
