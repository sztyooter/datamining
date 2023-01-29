package edu.bbte.data.beim1992.backend.dao;

import edu.bbte.data.beim1992.backend.model.Champion;
import edu.bbte.data.beim1992.backend.model.MatchUp;

import java.util.concurrent.ConcurrentHashMap;

public interface ChampionDAO extends GeneralDAO<Champion> {

    Champion findByName(String name);
}
