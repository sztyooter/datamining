package edu.bbte.data.beim1992.backend.controller;

import edu.bbte.data.beim1992.backend.dao.ChampionDAO;
import edu.bbte.data.beim1992.backend.dao.MatchUpDAO;
import edu.bbte.data.beim1992.backend.model.Champion;
import edu.bbte.data.beim1992.backend.model.MatchUp;
import edu.bbte.data.beim1992.backend.model.dto.MatchUpMapper;
import edu.bbte.data.beim1992.backend.model.dto.MatchUpOutDto;
import edu.bbte.data.beim1992.backend.model.exceptions.NotFoundException;
import edu.bbte.data.beim1992.backend.utils.Property;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/matchups")
@Slf4j
public class MatchUpsController {

    @Autowired
    private Property property;

    @Autowired
    private ChampionDAO championDAO;

    @Autowired
    private MatchUpMapper matchUpMapper;

    @Autowired
    private MatchUpDAO matchUpDAO;

    @CrossOrigin
    @GetMapping
    public ConcurrentHashMap<String, Collection<MatchUpOutDto>> getMatchUps(@RequestParam(required = true) String name, @RequestParam(required = true) String against) {

        Champion champion = championDAO.findByName(against);

        if (champion == null) {

            throw new NotFoundException();
        }

        ConcurrentHashMap<String, Collection<MatchUpOutDto>> graph = new ConcurrentHashMap<>();

        graph.put(against, matchUpMapper.matchUpsToDtos(matchUpDAO.findMatchUps(champion).stream().filter(matchUp -> matchUp.getNumberOfGames() >= property.getMinimumGames()).collect(Collectors.toList())));

        for (MatchUpOutDto matchUp: graph.get(against)) {

            LinkedList<MatchUpOutDto> list = new LinkedList<>();

            for (MatchUpOutDto matchUp1: graph.get(against)) {

                if (!matchUp.equals(matchUp1)) {

                    Champion champion1 = championDAO.findByName(matchUp.getAgainst().getName());
                    Champion champion2 = championDAO.findByName(matchUp1.getAgainst().getName());

                    MatchUp winRate = matchUpDAO.find(champion1, champion2);

                    if (winRate.getNumberOfGames() >= property.getMinimumGames() && (float)winRate.getNumberOfWins() / winRate.getNumberOfGames() > 0.5) {

                        list.add(matchUpMapper.matchUpToDto(winRate));

                    } else {

                        //list.add(matchUpMapper.matchUpToDto(new MatchUp(champion2, 1, 2)));
                    }
                }
            }

            Champion champion1 = championDAO.findByName(matchUp.getAgainst().getName());

            MatchUp winRate = matchUpDAO.find(champion1, champion);

            if (winRate.getNumberOfGames() >= property.getMinimumGames()) {

                list.add(matchUpMapper.matchUpToDto(winRate));
            }

            graph.put(matchUp.getAgainst().getName(), list);
        }

        graph.put(against, graph.get(against).stream().filter(matchUpOutDto -> (float)matchUpOutDto.getNumberOfWins() / matchUpOutDto.getNumberOfGames() > 0.5).toList());

        return graph;
    }
}
