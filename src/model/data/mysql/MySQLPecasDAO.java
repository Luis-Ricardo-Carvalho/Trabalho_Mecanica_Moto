package model.data.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.ModelException;
import model.Pecas;
import model.User;
import model.data.DAOFactory;
import model.data.DAOUtils;
import model.data.PecasDAO;
import model.data.mysql.utils.MySQLConnectionFactory;

public class MySQLPecasDAO implements PecasDAO {

    @Override
    public void save(Pecas peca) throws ModelException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = MySQLConnectionFactory.getConnection();

            String sqlInsert = "INSERT INTO pecas (nome, marca, categoria, quantidade, user_id) "
                    + "VALUES (?, ?, ?, ?, ?);";

            preparedStatement = connection.prepareStatement(sqlInsert);
            preparedStatement.setString(1, peca.getName());
            preparedStatement.setString(2, peca.getMarca());
            preparedStatement.setString(3, peca.getCategoria());
            preparedStatement.setInt(4, peca.getQuantidade());
            preparedStatement.setInt(5, peca.getUser().getId());

            preparedStatement.executeUpdate();

        } catch (SQLException sqle) {
            DAOUtils.sqlExceptionTreatement("Erro ao inserir peça no BD.", sqle);
        } catch (ModelException me) {
            throw me;
        } finally {
            DAOUtils.close(preparedStatement);
            DAOUtils.close(connection);
        }
    }

    @Override
    public void update(Pecas peca) throws ModelException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = MySQLConnectionFactory.getConnection();

            String sqlUpdate = "UPDATE pecas "
                    + "SET nome = ?, marca = ?, categoria = ?, quantidade = ?, user_id = ? "
                    + "WHERE id = ?;";

            preparedStatement = connection.prepareStatement(sqlUpdate);
            preparedStatement.setString(1, peca.getName());
            preparedStatement.setString(2, peca.getMarca());
            preparedStatement.setString(3, peca.getCategoria());
            preparedStatement.setInt(4, peca.getQuantidade());
            preparedStatement.setInt(5, peca.getUser().getId());
            preparedStatement.setInt(6, peca.getId());

            preparedStatement.executeUpdate();

        } catch (SQLException sqle) {
            DAOUtils.sqlExceptionTreatement("Erro ao atualizar peça no BD.", sqle);
        } finally {
            DAOUtils.close(preparedStatement);
            DAOUtils.close(connection);
        }
    }

    @Override
    public void delete(Pecas peca) throws ModelException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = MySQLConnectionFactory.getConnection();

            String sqlDelete = "DELETE FROM pecas WHERE id = ?;";

            preparedStatement = connection.prepareStatement(sqlDelete);
            preparedStatement.setInt(1, peca.getId());

            preparedStatement.executeUpdate();

        } catch (SQLException sqle) {
            DAOUtils.sqlExceptionTreatement("Erro ao excluir peça do BD.", sqle);
        } finally {
            DAOUtils.close(preparedStatement);
            DAOUtils.close(connection);
        }
    }

    @Override
    public List<Pecas> findAll() throws ModelException {
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        List<Pecas> pecasList = new ArrayList<>();

        try {
            connection = MySQLConnectionFactory.getConnection();

            statement = connection.createStatement();
            String sqlSelect = "SELECT * FROM pecas ORDER BY nome ASC;";

            rs = statement.executeQuery(sqlSelect);
            setUpUsers(rs, pecasList);

        } catch (SQLException sqle) {
            DAOUtils.sqlExceptionTreatement("Erro ao carregar peças do BD.", sqle);
        } finally {
            DAOUtils.close(rs);
            DAOUtils.close(statement);
            DAOUtils.close(connection);
        }

        return pecasList;
    }

    @Override
    public List<Pecas> findByUserId(int userId) throws ModelException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        List<Pecas> pecasList = new ArrayList<>();

        try {
            connection = MySQLConnectionFactory.getConnection();

            String sqlSelect = "SELECT * FROM pecas WHERE user_id = ?;";
            preparedStatement = connection.prepareStatement(sqlSelect);
            preparedStatement.setInt(1, userId);

            rs = preparedStatement.executeQuery();
            setUpUsers(rs, pecasList);

        } catch (SQLException sqle) {
            DAOUtils.sqlExceptionTreatement("Erro ao carregar peças do BD.", sqle);
        } finally {
            DAOUtils.close(rs);
            DAOUtils.close(preparedStatement);
            DAOUtils.close(connection);
        }

        return pecasList;
    }

    private void setUpUsers(ResultSet rs, List<Pecas> pecasList)
            throws SQLException, ModelException {

        while (rs.next()) {
            int id = rs.getInt("id");
            String nome = rs.getString("nome");
            String marca = rs.getString("marca");
            String categoria = rs.getString("categoria");
            int quantidade = rs.getInt("quantidade");
            int userId = rs.getInt("user_id");

            Pecas peca = new Pecas(id);
            peca.setName(nome);
            peca.setMarca(marca);
            peca.setCategoria(categoria);
            peca.setQuantidade(quantidade);

            User user = DAOFactory.createUserNDAO().findById(userId);
            peca.setUser(user);;

            pecasList.add(peca);
        }
    }

    @Override
    public Pecas findById(int id) throws ModelException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        Pecas peca = null;

        try {
            connection = MySQLConnectionFactory.getConnection();

            String sqlSelect = "SELECT * FROM pecas WHERE id = ?;";
            preparedStatement = connection.prepareStatement(sqlSelect);
            preparedStatement.setInt(1, id);

            rs = preparedStatement.executeQuery();

            if (rs.next()) {
                int pecaId = rs.getInt("id");
                String nome = rs.getString("nome");
                String marca = rs.getString("marca");
                String categoria = rs.getString("categoria");
                int quantidade = rs.getInt("quantidade");
                int userId = rs.getInt("user_id");

                peca = new Pecas(pecaId);
                peca.setName(nome);
                peca.setMarca(marca);
                peca.setCategoria(categoria);
                peca.setQuantidade(quantidade);

                peca.setUser(DAOFactory.createUserNDAO().findById(userId));
            }

        } catch (SQLException sqle) {
            DAOUtils.sqlExceptionTreatement("Erro ao buscar peça por ID no BD.", sqle);
        } finally {
            DAOUtils.close(rs);
            DAOUtils.close(preparedStatement);
            DAOUtils.close(connection);
        }

        return peca;
    }

    
}