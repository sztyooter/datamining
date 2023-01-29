package edu.bbte.data.beim1992.backend.dao;

import edu.bbte.data.beim1992.backend.model.Champion;
import edu.bbte.data.beim1992.backend.model.Match;
import edu.bbte.data.beim1992.backend.model.MatchUp;
import edu.bbte.data.beim1992.backend.model.Summoner;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public interface MatchDAO extends GeneralDAO<Match> {

    Match findByMatchId(String matchId);

    void playedIn(Match match, Summoner summoner, String role);

    List<Match> wasPlayedIn(Champion champion);

    ConcurrentHashMap<String, ConcurrentHashMap<String, MatchUp>> getWinRates();

    void updateMatchUps();
}
