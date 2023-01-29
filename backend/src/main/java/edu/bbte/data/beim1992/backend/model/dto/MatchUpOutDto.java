package edu.bbte.data.beim1992.backend.model.dto;

import lombok.Data;

@Data
public class MatchUpOutDto {

    private ChampionOutDto against;
    private int numberOfWins;
    private int numberOfGames;
}
