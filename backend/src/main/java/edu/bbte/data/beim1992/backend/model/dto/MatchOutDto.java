package edu.bbte.data.beim1992.backend.model.dto;

import lombok.Data;

@Data
public class MatchOutDto {

    private String matchID;

    private ChampionOutDto winnerTop;
    private ChampionOutDto winnerJung;
    private ChampionOutDto winnerMid;
    private ChampionOutDto winnerBot;
    private ChampionOutDto winnerSupp;

    private ChampionOutDto loserTop;
    private ChampionOutDto loserJung;
    private ChampionOutDto loserMid;
    private ChampionOutDto loserBot;
    private ChampionOutDto loserSupp;
}
