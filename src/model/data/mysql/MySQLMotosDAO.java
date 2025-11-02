package model.data.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.ModelException;
import model.Motos;
import model.Pecas;
import model.data.DAOFactory;
import model.data.DAOUtils;
import model.data.MotosDAO;
import model.data.mysql.utils.MySQLConnectionFactory;

public class MySQLMotosDAO implements MotosDAO {

    @Override
    public void save(Motos moto) throws ModelException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = MySQLConnectionFactory.getConnection();

            String sqlInsert = "INSERT INTO motos (modelo, marca, descricao, pecas_id) "
                    + "VALUES (?, ?, ?, ?);";

            preparedStatement = connection.prepareStatement(sqlInsert);
            preparedStatement.setString(1, moto.getModelo());
            preparedStatement.setString(2, moto.getMarca());
            preparedStatement.setString(3, moto.getDescricao());
            preparedStatement.setInt(4, moto.getPeca().getId());

            preparedStatement.executeUpdate();

        } catch (SQLException sqle) {
            DAOUtils.sqlExceptionTreatement("Erro ao inserir moto no BD.", sqle);
        } finally {
            DAOUtils.close(preparedStatement);
            DAOUtils.close(connection);
        }
    }

    @Override
    public void update(Motos moto) throws ModelException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = MySQLConnectionFactory.getConnection();

            String sqlUpdate = "UPDATE motos "
                    + "SET modelo = ?, marca = ?, descricao = ?, pecas_id = ? "
                    + "WHERE id = ?;";

            preparedStatement = connection.prepareStatement(sqlUpdate);
            preparedStatement.setString(1, moto.getModelo());
            preparedStatement.setString(2, moto.getMarca());
            preparedStatement.setString(3, moto.getDescricao());
            preparedStatement.setInt(4, moto.getPeca().getId());
            preparedStatement.setInt(5, moto.getId());

            preparedStatement.executeUpdate();

        } catch (SQLException sqle) {
            DAOUtils.sqlExceptionTreatement("Erro ao atualizar moto no BD.", sqle);
        } finally {
            DAOUtils.close(preparedStatement);
            DAOUtils.close(connection);
        }
    }

    @Override
    public void delete(Motos moto) throws ModelException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = MySQLConnectionFactory.getConnection();

            String sqlDelete = "DELETE FROM motos WHERE id = ?;";

            preparedStatement = connection.prepareStatement(sqlDelete);
            preparedStatement.setInt(1, moto.getId());

            preparedStatement.executeUpdate();

        } catch (SQLException sqle) {
            DAOUtils.sqlExceptionTreatement("Erro ao excluir moto do BD.", sqle);
        } finally {
            DAOUtils.close(preparedStatement);
            DAOUtils.close(connection);
        }
    }

    @Override
    public List<Motos> findAll() throws ModelException {
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        List<Motos> motosList = new ArrayList<>();

        try {
            connection = MySQLConnectionFactory.getConnection();

            statement = connection.createStatement();
            String sqlSelect = "SELECT * FROM motos ORDER BY marca, modelo ASC;";

            rs = statement.executeQuery(sqlSelect);
            setUpPecas(rs, motosList);

        } catch (SQLException sqle) {
            DAOUtils.sqlExceptionTreatement("Erro ao carregar motos do BD.", sqle);
        } finally {
            DAOUtils.close(rs);
            DAOUtils.close(statement);
            DAOUtils.close(connection);
        }

        return motosList;
    }

    @Override
    public List<Motos> findByPecasId(int pecaId) throws ModelException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        List<Motos> motosList = new ArrayList<>();

        try {
            connection = MySQLConnectionFactory.getConnection();

            String sqlSelect = "SELECT * FROM motos WHERE pecas_id = ?;";
            preparedStatement = connection.prepareStatement(sqlSelect);
            preparedStatement.setInt(1, pecaId);

            rs = preparedStatement.executeQuery();
            setUpPecas(rs, motosList);

        } catch (SQLException sqle) {
            DAOUtils.sqlExceptionTreatement("Erro ao carregar motos do BD.", sqle);
        } finally {
            DAOUtils.close(rs);
            DAOUtils.close(preparedStatement);
            DAOUtils.close(connection);
        }

        return motosList;
    }

    private void setUpPecas(ResultSet rs, List<Motos> motosList)
            throws SQLException, ModelException {

        while (rs.next()) {
            int id = rs.getInt("id");
            String modelo = rs.getString("modelo");
            String marca = rs.getString("marca");
            String descricao = rs.getString("descricao");
            int pecasId = rs.getInt("pecas_id");

            Motos moto = new Motos(id);
            moto.setModelo(modelo);
            moto.setMarca(marca);
            moto.setDescricao(descricao);

            // Busca a peça associada à moto
            Pecas peca = DAOFactory.createPecasDAO().findById(pecasId);
            moto.setPeca(peca);

            motosList.add(moto);
        }
    }
    @Override
    public Motos findById(int id) throws ModelException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        Motos moto = null;

        try {
            connection = MySQLConnectionFactory.getConnection();

            String sqlSelect = "SELECT * FROM motos WHERE id = ?;";
            preparedStatement = connection.prepareStatement(sqlSelect);
            preparedStatement.setInt(1, id);

            rs = preparedStatement.executeQuery();

            if (rs.next()) {
                int motoId = rs.getInt("id");
                String modelo = rs.getString("modelo");
                String marca = rs.getString("marca");
                String descricao = rs.getString("descricao");
                int pecasId = rs.getInt("pecas_id");

                moto = new Motos(motoId);
                moto.setModelo(modelo);
                moto.setMarca(marca);
                moto.setDescricao(descricao);

                // Busca a peça associada à moto
                moto.setPeca(DAOFactory.createPecasDAO().findById(pecasId));
            }

        } catch (SQLException sqle) {
            DAOUtils.sqlExceptionTreatement("Erro ao buscar moto por ID no BD.", sqle);
        } finally {
            DAOUtils.close(rs);
            DAOUtils.close(preparedStatement);
            DAOUtils.close(connection);
        }

        return moto;
    }

}
