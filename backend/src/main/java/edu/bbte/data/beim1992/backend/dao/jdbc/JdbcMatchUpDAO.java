package edu.bbte.data.beim1992.backend.dao.jdbc;

import edu.bbte.data.beim1992.backend.dao.ChampionDAO;
import edu.bbte.data.beim1992.backend.dao.DaoException;
import edu.bbte.data.beim1992.backend.dao.MatchUpDAO;
import edu.bbte.data.beim1992.backend.model.Champion;
import edu.bbte.data.beim1992.backend.model.MatchUp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;

@Repository
@Slf4j
@Profile("jdbc")
public class JdbcMatchUpDAO implements MatchUpDAO {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private ChampionDAO championDAO;

    public JdbcMatchUpDAO() {

        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            throw new DaoException("Could not load driver class", e);
        }
    }


    @Override
    public MatchUp find(Champion champion, Champion against) {

        try (Connection con = dataSource.getConnection();) {
            PreparedStatement preparedStatement = con.prepareStatement("SELECT * FROM matchups WHERE champion_id = ? AND against_id  = ?");
            preparedStatement.setLong(1, champion.getId());
            preparedStatement.setLong(2, against.getId());
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return parseResultSet(resultSet);
            }

            return null;
        } catch (SQLException e) {
            log.error("Query failed.", e);
            throw new DaoException("Could not return match up.", e);
        }
    }

    @Override
    public void create(Champion champion, MatchUp matchUp) {

        try (Connection con = dataSource.getConnection();) {
            PreparedStatement preparedStatement = con.prepareStatement(
                    "INSERT INTO matchups(champion_id, against_id, number_of_games, number_of_wins) VALUES (?, ?, ?, ?)");

            preparedStatement.setLong(1, champion.getId());
            preparedStatement.setLong(2, matchUp.getAgainst().getId());
            preparedStatement.setInt(3, matchUp.getNumberOfGames());
            preparedStatement.setInt(4, matchUp.getNumberOfWins());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            log.error("Create failed.", e);
            throw new DaoException("Could not insert match up.", e);
        }
    }

    @Override
    public void update(Champion champion, MatchUp matchUp) {

        try (Connection con = dataSource.getConnection();) {
            PreparedStatement preparedStatement = con.prepareStatement(
                    "UPDATE matchups SET number_of_games = ?, number_of_wins = ? WHERE champion_id = ? AND against_id = ?");

            preparedStatement.setInt(1, matchUp.getNumberOfGames());
            preparedStatement.setInt(2, matchUp.getNumberOfWins());
            preparedStatement.setLong(3, champion.getId());
            preparedStatement.setLong(4, matchUp.getAgainst().getId());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            log.error("Create failed.", e);
            throw new DaoException("Could not update match up.", e);
        }
    }

    @Override
    public Collection<MatchUp> findMatchUps(Champion champion) {

        try (Connection con = dataSource.getConnection();) {
            PreparedStatement preparedStatement = con.prepareStatement("SELECT * FROM matchups WHERE champion_id = ?");
            preparedStatement.setLong(1, champion.getId());
            ResultSet resultSet = preparedStatement.executeQuery();

            LinkedList<MatchUp> list = new LinkedList<>();

            while (resultSet.next()) {
                list.add(parseResultSet(resultSet));
            }

            return list;
        } catch (SQLException e) {
            log.error("Query failed.", e);
            throw new DaoException("Could not return match ups.", e);
        }
    }

    private MatchUp parseResultSet(ResultSet resultSet) throws SQLException {

        MatchUp matchUp = new MatchUp();

        matchUp.setAgainst(championDAO.findById(resultSet.getLong("against_id")));
        matchUp.setNumberOfGames(resultSet.getInt("number_of_games"));
        matchUp.setNumberOfWins(resultSet.getInt("number_of_wins"));

        return matchUp;
    }
}
