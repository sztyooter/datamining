package edu.bbte.data.beim1992.backend.dao;

import edu.bbte.data.beim1992.backend.model.Champion;
import edu.bbte.data.beim1992.backend.model.MatchUp;

import java.util.Collection;

public interface MatchUpDAO {

    MatchUp find(Champion champion, Champion against);

    Collection<MatchUp> findMatchUps(Champion champion);

    void create(Champion champion, MatchUp matchUp);

    void update(Champion champion, MatchUp matchUp);
}
