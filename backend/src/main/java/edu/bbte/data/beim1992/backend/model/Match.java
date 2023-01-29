package edu.bbte.data.beim1992.backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Match extends BaseEntity {

    private String matchID;

    private Champion winnerTop;
    private Champion winnerJung;
    private Champion winnerMid;
    private Champion winnerBot;
    private Champion winnerSupp;

    private Champion loserTop;
    private Champion loserJung;
    private Champion loserMid;
    private Champion loserBot;
    private Champion loserSupp;
}
