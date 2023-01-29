package edu.bbte.data.beim1992.backend.dao.jdbc;

import edu.bbte.data.beim1992.backend.dao.ChampionDAO;
import edu.bbte.data.beim1992.backend.dao.DaoException;
import edu.bbte.data.beim1992.backend.dao.MatchDAO;
import edu.bbte.data.beim1992.backend.dao.MatchUpDAO;
import edu.bbte.data.beim1992.backend.model.Champion;
import edu.bbte.data.beim1992.backend.model.Match;
import edu.bbte.data.beim1992.backend.model.MatchUp;
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
import java.util.concurrent.ConcurrentHashMap;

@Repository
@Slf4j
@Profile("jdbc")
public class JdbcMatchDAO implements MatchDAO {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private ChampionDAO championDAO;

    @Autowired
    private MatchUpDAO matchUpDAO;

    public JdbcMatchDAO() {

        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            throw new DaoException("Could not load driver class", e);
        }
    }

    @Override
    public List<Match> findAll() {
        try (Connection con = dataSource.getConnection()) {
            LinkedList<Match> list = new LinkedList<>();
            ResultSet resultSet = con.prepareStatement("SELECT * FROM matches").executeQuery();

            while (resultSet.next()) {
                list.add(parseResultSet(resultSet));
            }

            return list;
        } catch (SQLException e) {
            log.error("Query failed.", e);
            throw new DaoException("Could not return list of matches", e);
        }
    }

    @Override
    public Match findById(Long id) {
        try (Connection con = dataSource.getConnection();) {
            PreparedStatement preparedStatement = con.prepareStatement("SELECT * FROM matches WHERE id = ?");
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return parseResultSet(resultSet);
            }

            return null;
        } catch (SQLException e) {
            log.error("Query failed.", e);
            throw new DaoException("Could not return match.", e);
        }
    }

    @Override
    public Match findByMatchId(String matchId) {
        try (Connection con = dataSource.getConnection();) {
            PreparedStatement preparedStatement = con.prepareStatement("SELECT * FROM matches WHERE matchID = ?");
            preparedStatement.setString(1, matchId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return parseResultSet(resultSet);
            }

            return null;
        } catch (SQLException e) {
            log.error("Query failed.", e);
            throw new DaoException("Could not return match.", e);
        }
    }

    @Override
    public void create(Match entity) {
        try (Connection con = dataSource.getConnection();) {
            PreparedStatement preparedStatement = con.prepareStatement(
                    "INSERT INTO matches(matchID, winnerTop, winnerJung, winnerMid, winnerBot, winnerSupp," +
                            "loserTop, loserJung, loserMid, loserBot, loserSupp) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

            preparedStatement.setString(1, entity.getMatchID());

            preparedStatement.setLong(2, entity.getWinnerTop().getId());
            preparedStatement.setLong(3, entity.getWinnerJung().getId());
            preparedStatement.setLong(4, entity.getWinnerMid().getId());
            preparedStatement.setLong(5, entity.getWinnerBot().getId());
            preparedStatement.setLong(6, entity.getWinnerSupp().getId());
            preparedStatement.setLong(7, entity.getLoserTop().getId());
            preparedStatement.setLong(8, entity.getLoserJung().getId());
            preparedStatement.setLong(9, entity.getLoserMid().getId());
            preparedStatement.setLong(10, entity.getLoserBot().getId());
            preparedStatement.setLong(11, entity.getLoserSupp().getId());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            log.error("Create failed.", e);
            throw new DaoException("Could not insert match.", e);
        }
    }

    @Override
    public void update(Match entity, Long id) {

        log.error("Update match not implemented!");
    }

    @Override
    public void delete(Long id) {

        try (Connection con = dataSource.getConnection();) {
            PreparedStatement preparedStatement = con.prepareStatement("DELETE FROM matches where id = ?");
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            log.error("Delete failed.", e);
            throw new DaoException("Could not delete match.", e);
        }
    }

    @Override
    public void playedIn(Match entity, Summoner summoner, String role) {

        try (Connection con = dataSource.getConnection();) {
            PreparedStatement preparedStatement = con.prepareStatement(
                    "INSERT INTO played(summoner_id, match_id, position) VALUES (?, ?, ?)");

            preparedStatement.setLong(1, summoner.getId());
            preparedStatement.setLong(2, entity.getId());
            preparedStatement.setString(3, role);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            log.error("Query failed.", e);
            throw new DaoException("Could not insert played in row.", e);
        }
    }

    @Override
    public List<Match> wasPlayedIn(Champion champion) {

        try (Connection con = dataSource.getConnection()) {
            LinkedList<Match> list = new LinkedList<>();

            PreparedStatement preparedStatement = con.prepareStatement("SELECT * FROM matches WHERE" +
                    "(winnerTop = ? OR winnerJung = ? OR winnerMid = ? OR winnerBot = ? OR winnerSupp = ? OR" +
                    " loserTop = ? OR loserJung = ? OR loserMid = ? OR loserBot = ? OR loserSupp = ?)");

            preparedStatement.setLong(1, champion.getId());
            preparedStatement.setLong(2, champion.getId());
            preparedStatement.setLong(3, champion.getId());
            preparedStatement.setLong(4, champion.getId());
            preparedStatement.setLong(5, champion.getId());
            preparedStatement.setLong(6, champion.getId());
            preparedStatement.setLong(7, champion.getId());
            preparedStatement.setLong(8, champion.getId());
            preparedStatement.setLong(9, champion.getId());
            preparedStatement.setLong(10, champion.getId());

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                list.add(parseResultSet(resultSet));
            }

            return list;
        } catch (SQLException e) {
            log.error("Query failed.", e);
            throw new DaoException("Could not return list of matches", e);
        }
    }

    @Override
    public ConcurrentHashMap<String, ConcurrentHashMap<String, MatchUp>> getWinRates() {

        ConcurrentHashMap<String, ConcurrentHashMap<String, MatchUp>> ret = new ConcurrentHashMap<>();

        for (Champion champion: championDAO.findAll()) {

            ConcurrentHashMap<String, MatchUp> list = new ConcurrentHashMap<>();

            for (Champion champion1: championDAO.findAll()) {
                if (!champion.equals(champion1)) {

                    MatchUp matchUp = matchUpDAO.find(champion, champion1);

                    if (matchUp == null) {

                        list.put(champion1.getName(), new MatchUp(champion1, 0, 0));

                    } else {

                        list.put(champion1.getName(), matchUp);
                    }
                }
            }

            ret.put(champion.getName(), list);
        }

        return ret;
    }

    public void updateMatchUps() {

        log.info("Updating match ups");

        ConcurrentHashMap<String, ConcurrentHashMap<String, MatchUp>> ret = new ConcurrentHashMap<>();

        for (Champion champion: championDAO.findAll()) {

            ConcurrentHashMap<String, MatchUp> matchUps = new ConcurrentHashMap<>();

            for (Champion champion1: championDAO.findAll()) {

                matchUps.put(champion1.getName(), new MatchUp(champion1, 0, 0));
            }

            ret.put(champion.getName(), matchUps);
        }

        for (Match match: findAll()) {

            log.info(match.getMatchID());

            //TOP
            if (match.getWinnerTop() != null && match.getLoserTop() != null) {
                MatchUp matchUp1 = ret.get(match.getWinnerTop().getName()).get(match.getLoserTop().getName());
                matchUp1.setNumberOfGames(matchUp1.getNumberOfGames() + 1);
                matchUp1.setNumberOfWins(matchUp1.getNumberOfWins() + 1);
                MatchUp matchUp2 = ret.get(match.getLoserTop().getName()).get(match.getWinnerTop().getName());
                matchUp2.setNumberOfGames(matchUp2.getNumberOfGames() + 1);

                ret.get(match.getWinnerTop().getName()).put(match.getLoserTop().getName(), matchUp1);
                ret.get(match.getLoserTop().getName()).put(match.getWinnerTop().getName(), matchUp2);
            }

            //JUNGLE
            if (match.getWinnerJung() != null && match.getLoserJung() != null) {
                MatchUp matchUp1 = ret.get(match.getWinnerJung().getName()).get(match.getLoserJung().getName());
                matchUp1.setNumberOfGames(matchUp1.getNumberOfGames() + 1);
                matchUp1.setNumberOfWins(matchUp1.getNumberOfWins() + 1);
                MatchUp matchUp2 = ret.get(match.getLoserJung().getName()).get(match.getWinnerJung().getName());
                matchUp2.setNumberOfGames(matchUp2.getNumberOfGames() + 1);

                ret.get(match.getWinnerJung().getName()).put(match.getLoserJung().getName(), matchUp1);
                ret.get(match.getLoserJung().getName()).put(match.getWinnerJung().getName(), matchUp2);
            }

            //MID
            if (match.getWinnerMid() != null && match.getLoserMid() != null) {
                MatchUp matchUp1 = ret.get(match.getWinnerMid().getName()).get(match.getLoserMid().getName());
                matchUp1.setNumberOfGames(matchUp1.getNumberOfGames() + 1);
                matchUp1.setNumberOfWins(matchUp1.getNumberOfWins() + 1);
                MatchUp matchUp2 = ret.get(match.getLoserMid().getName()).get(match.getWinnerMid().getName());
                matchUp2.setNumberOfGames(matchUp2.getNumberOfGames() + 1);

                ret.get(match.getWinnerMid().getName()).put(match.getLoserMid().getName(), matchUp1);
                ret.get(match.getLoserMid().getName()).put(match.getWinnerMid().getName(), matchUp2);
            }

            //BOT
            if (match.getWinnerBot() != null && match.getLoserBot() != null) {
                MatchUp matchUp1 = ret.get(match.getWinnerBot().getName()).get(match.getLoserBot().getName());
                matchUp1.setNumberOfGames(matchUp1.getNumberOfGames() + 1);
                matchUp1.setNumberOfWins(matchUp1.getNumberOfWins() + 1);
                MatchUp matchUp2 = ret.get(match.getLoserBot().getName()).get(match.getWinnerBot().getName());
                matchUp2.setNumberOfGames(matchUp2.getNumberOfGames() + 1);

                ret.get(match.getWinnerBot().getName()).put(match.getLoserBot().getName(), matchUp1);
                ret.get(match.getLoserBot().getName()).put(match.getWinnerBot().getName(), matchUp2);
            }

            //SUPPORT
            if (match.getWinnerSupp() != null && match.getLoserSupp() != null) {
                MatchUp matchUp1 = ret.get(match.getWinnerSupp().getName()).get(match.getLoserSupp().getName());
                matchUp1.setNumberOfGames(matchUp1.getNumberOfGames() + 1);
                matchUp1.setNumberOfWins(matchUp1.getNumberOfWins() + 1);
                MatchUp matchUp2 = ret.get(match.getLoserSupp().getName()).get(match.getWinnerSupp().getName());
                matchUp2.setNumberOfGames(matchUp2.getNumberOfGames() + 1);

                ret.get(match.getWinnerSupp().getName()).put(match.getLoserSupp().getName(), matchUp1);
                ret.get(match.getLoserSupp().getName()).put(match.getWinnerSupp().getName(), matchUp2);
            }
        }

        for (Champion champion: championDAO.findAll()) {
            for (Champion champion1: championDAO.findAll()) {
               if (!champion.equals(champion1)) {

                    MatchUp matchUp = matchUpDAO.find(champion, champion1);

                    if (matchUp == null) {

                        matchUpDAO.create(champion, ret.get(champion.getName()).get(champion1.getName()));

                    } else {

                        matchUpDAO.update(champion, ret.get(champion.getName()).get(champion1.getName()));
                    }
               }
            }
        }

        log.info("Match ups updated");
    }

    private Match parseResultSet(ResultSet resultSet) throws SQLException {

        Match match = new Match();

        match.setId(resultSet.getLong("id"));
        match.setMatchID(resultSet.getString("matchID"));

        match.setWinnerTop(championDAO.findById(resultSet.getLong("winnerTop")));
        match.setWinnerJung(championDAO.findById(resultSet.getLong("winnerJung")));
        match.setWinnerMid(championDAO.findById(resultSet.getLong("winnerMid")));
        match.setWinnerBot(championDAO.findById(resultSet.getLong("winnerBot")));
        match.setWinnerSupp(championDAO.findById(resultSet.getLong("winnerSupp")));

        match.setLoserTop(championDAO.findById(resultSet.getLong("loserTop")));
        match.setLoserJung(championDAO.findById(resultSet.getLong("loserJung")));
        match.setLoserMid(championDAO.findById(resultSet.getLong("loserMid")));
        match.setLoserBot(championDAO.findById(resultSet.getLong("loserBot")));
        match.setLoserSupp(championDAO.findById(resultSet.getLong("loserSupp")));

        return match;
    }
}
