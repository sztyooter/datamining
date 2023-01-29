package edu.bbte.data.beim1992.backend.dao.jdbc;

import edu.bbte.data.beim1992.backend.dao.ChampionDAO;
import edu.bbte.data.beim1992.backend.dao.DaoException;
import edu.bbte.data.beim1992.backend.model.Champion;
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
public class JdbcChampionDAO implements ChampionDAO {

    @Autowired
    private DataSource dataSource;

    public JdbcChampionDAO() {

        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            throw new DaoException("Could not load driver class", e);
        }
    }

    @Override
    public List<Champion> findAll() {

        try (Connection con = dataSource.getConnection()) {
            LinkedList<Champion> list = new LinkedList<>();
            ResultSet resultSet = con.prepareStatement("SELECT * FROM champions").executeQuery();

            while (resultSet.next()) {
                list.add(parseResultSet(resultSet));
            }

            return list;
        } catch (SQLException e) {
            log.error("Query failed.", e);
            throw new DaoException("Could not return list of champions", e);
        }
    }

    @Override
    public Champion findById(Long id) {

        try (Connection con = dataSource.getConnection();) {
            PreparedStatement preparedStatement = con.prepareStatement("SELECT * FROM champions WHERE id = ?");
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return parseResultSet(resultSet);
            }

            return null;
        } catch (SQLException e) {
            log.error("Query failed.", e);
            throw new DaoException("Could not return champion.", e);
        }
    }

    @Override
    public Champion findByName(String name) {

        try (Connection con = dataSource.getConnection();) {
            PreparedStatement preparedStatement = con.prepareStatement("SELECT * FROM champions WHERE name = ?");
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return parseResultSet(resultSet);
            }

            return null;
        } catch (SQLException e) {
            log.error("Query failed.", e);
            throw new DaoException("Could not return champion.", e);
        }
    }

    @Override
    public void create(Champion entity) {

        try (Connection con = dataSource.getConnection();) {
            PreparedStatement preparedStatement = con.prepareStatement(
                    "INSERT INTO champions(name) VALUES (?)");

            preparedStatement.setString(1, entity.getName());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            log.error("Create failed.", e);
            throw new DaoException("Could not insert champion.", e);
        }
    }

    @Override
    public void update(Champion entity, Long id) {

        try (Connection con = dataSource.getConnection();) {
            PreparedStatement preparedStatement = con.prepareStatement("UPDATE champions SET name = ? WHERE id = ?");

            preparedStatement.setString(1, entity.getName());
            preparedStatement.setLong(2, id);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            log.error("Update failed.", e);
            throw new DaoException("Could not update champion.", e);
        }
    }

    @Override
    public void delete(Long id) {

        try (Connection con = dataSource.getConnection();) {
            PreparedStatement preparedStatement = con.prepareStatement("DELETE FROM champions where id = ?");
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            log.error("Delete failed.", e);
            throw new DaoException("Could not delete champion.", e);
        }
    }

    private Champion parseResultSet(ResultSet resultSet) throws SQLException {

        Champion champion = new Champion();

        champion.setId(resultSet.getLong("id"));
        champion.setName(resultSet.getString("name"));

        return champion;
    }
}
