package edu.bbte.data.beim1992.backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class MatchUp {

    private Champion against;
    private int numberOfWins;
    private int numberOfGames;
}
