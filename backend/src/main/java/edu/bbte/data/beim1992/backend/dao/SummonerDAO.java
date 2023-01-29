package edu.bbte.data.beim1992.backend.dao;

import edu.bbte.data.beim1992.backend.model.Summoner;

public interface SummonerDAO extends GeneralDAO<Summoner> {

    Summoner findByName(String name);

    Summoner findByPuuid(String puuid);
}
