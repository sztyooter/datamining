package edu.bbte.data.beim1992.backend.controller;

import edu.bbte.data.beim1992.backend.dao.ChampionDAO;
import edu.bbte.data.beim1992.backend.dao.MatchDAO;
import edu.bbte.data.beim1992.backend.model.Champion;
import edu.bbte.data.beim1992.backend.model.Match;
import edu.bbte.data.beim1992.backend.model.MatchUp;
import edu.bbte.data.beim1992.backend.model.dto.MatchUpMapper;
import edu.bbte.data.beim1992.backend.model.dto.MatchUpOutDto;
import edu.bbte.data.beim1992.backend.model.exceptions.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/matchup")
@Slf4j
public class MatchUpController {

    @Autowired
    private ChampionDAO championDAO;

    @Autowired
    private MatchDAO matchDAO;

    @Autowired
    private MatchUpMapper matchUpMapper;

    @CrossOrigin
    @GetMapping
    public MatchUpOutDto getMatchUp(@RequestParam(required = true) String name, @RequestParam(required = true) String against) {

        Champion champion = championDAO.findByName(name);
        Champion enemy = championDAO.findByName(against);

        if (champion == null || enemy == null) {

            throw new NotFoundException();
        }

        MatchUp matchUp = new MatchUp(enemy, 0, 0);

        for (Match match: matchDAO.wasPlayedIn(champion)) {

            if ((match.getWinnerTop().equals(champion) && match.getLoserTop().equals(enemy)) ||
                    (match.getWinnerJung().equals(champion) && match.getLoserJung().equals(enemy)) ||
                    (match.getWinnerMid().equals(champion) && match.getLoserMid().equals(enemy)) ||
                    (match.getWinnerBot().equals(champion) && match.getLoserBot().equals(enemy)) ||
                    (match.getWinnerSupp().equals(champion) && match.getLoserSupp().equals(enemy))) {

                matchUp.setNumberOfWins(matchUp.getNumberOfWins() + 1);
                matchUp.setNumberOfGames(matchUp.getNumberOfGames() + 1);

            } else if ((match.getLoserTop().equals(champion) && match.getWinnerTop().equals(enemy)) ||
                    (match.getLoserJung().equals(champion) && match.getWinnerJung().equals(enemy)) ||
                    (match.getLoserMid().equals(champion) && match.getWinnerMid().equals(enemy)) ||
                    (match.getLoserBot().equals(champion) && match.getWinnerBot().equals(enemy)) ||
                    (match.getLoserSupp().equals(champion) && match.getWinnerSupp().equals(enemy))) {

                matchUp.setNumberOfGames(matchUp.getNumberOfGames() + 1);
            }
        }

        return matchUpMapper.matchUpToDto(matchUp);
    }
}
