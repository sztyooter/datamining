package edu.bbte.data.beim1992.backend.model.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class MatchInDto {

    @NotNull
    @Size(max = 255)
    private String matchID;

    @NotNull
    private ChampionInDto winnerTop;
    @NotNull
    private ChampionInDto winnerJung;
    @NotNull
    private ChampionInDto winnerMid;
    @NotNull
    private ChampionInDto winnerBot;
    @NotNull
    private ChampionInDto winnerSupp;

    @NotNull
    private ChampionInDto loserTop;
    @NotNull
    private ChampionInDto loserJung;
    @NotNull
    private ChampionInDto loserMid;
    @NotNull
    private ChampionInDto loserBot;
    @NotNull
    private ChampionInDto loserSupp;
}
