package edu.bbte.data.beim1992.backend.dao.jdbc;

import edu.bbte.data.beim1992.backend.dao.DaoException;
import edu.bbte.data.beim1992.backend.dao.SummonerDAO;
import edu.bbte.data.beim1992.backend.model.Summoner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

@Repository
@Slf4j
@Profile("jdbc")
public class JdbcSummonerDAO implements SummonerDAO {

    @Autowired
    private DataSource dataSource;

    public JdbcSummonerDAO() {

        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            throw new DaoException("Could not load driver class", e);
        }
    }

    @Override
    public List<Summoner> findAll() {
        try (Connection con = dataSource.getConnection()) {
            LinkedList<Summoner> list = new LinkedList<>();
            ResultSet resultSet = con.prepareStatement("SELECT * FROM summoners").executeQuery();

            while (resultSet.next()) {
                list.add(parseResultSet(resultSet));
            }

            return list;
        } catch (SQLException e) {
            log.error("Query failed.", e);
            throw new DaoException("Could not return list of summoners", e);
        }
    }

    @Override
    public Summoner findById(Long id) {
        try (Connection con = dataSource.getConnection();) {
            PreparedStatement preparedStatement = con.prepareStatement("SELECT * FROM summoners WHERE id = ?");
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return parseResultSet(resultSet);
            }

            return null;
        } catch (SQLException e) {
            log.error("Query failed.", e);
            throw new DaoException("Could not return summoner.", e);
        }
    }

    @Override
    public Summoner findByName(String name) {
        try (Connection con = dataSource.getConnection();) {
            PreparedStatement preparedStatement = con.prepareStatement("SELECT * FROM summoners WHERE name = ?");
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return parseResultSet(resultSet);
            }

            return null;
        } catch (SQLException e) {
            log.error("Query failed.", e);
            throw new DaoException("Could not return summoner.", e);
        }
    }

    @Override
    public Summoner findByPuuid(String puuid) {
        try (Connection con = dataSource.getConnection();) {
            PreparedStatement preparedStatement = con.prepareStatement("SELECT * FROM summoners WHERE puuid = ?");
            preparedStatement.setString(1, puuid);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return parseResultSet(resultSet);
            }

            return null;
        } catch (SQLException e) {
            log.error("Query failed.", e);
            throw new DaoException("Could not return summoner.", e);
        }
    }

    @Override
    public void create(Summoner entity) {

        try (Connection con = dataSource.getConnection();) {
            PreparedStatement preparedStatement = con.prepareStatement(
                    "INSERT INTO summoners(name, puuid) VALUES (?, ?)");

            preparedStatement.setString(1, entity.getName());
            preparedStatement.setString(2, entity.getPuuid());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            log.error("Create failed.", e);
            throw new DaoException("Could not insert summoner.", e);
        }
    }

    @Override
    public void update(Summoner entity, Long id) {

        try (Connection con = dataSource.getConnection();) {
            PreparedStatement preparedStatement = con.prepareStatement("UPDATE summoners SET name = ? puuid = ? WHERE id = ?");

            preparedStatement.setString(1, entity.getName());
            preparedStatement.setString(2, entity.getPuuid());
            preparedStatement.setLong(3, id);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            log.error("Update failed.", e);
            throw new DaoException("Could not update summoner.", e);
        }
    }

    @Override
    public void delete(Long id) {

        try (Connection con = dataSource.getConnection();) {
            PreparedStatement preparedStatement = con.prepareStatement("DELETE FROM summoners where id = ?");
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            log.error("Delete failed.", e);
            throw new DaoException("Could not delete summoner.", e);
        }
    }

    private Summoner parseResultSet(ResultSet resultSet) throws SQLException {

        Summoner summoner = new Summoner();

        summoner.setId(resultSet.getLong("id"));
        summoner.setName(resultSet.getString("name"));
        summoner.setPuuid(resultSet.getString("puuid"));

        return summoner;
    }
}
