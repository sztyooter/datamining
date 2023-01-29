package edu.bbte.data.beim1992.backend.controller;

import edu.bbte.data.beim1992.backend.dao.ChampionDAO;
import edu.bbte.data.beim1992.backend.model.dto.ChampionMapper;
import edu.bbte.data.beim1992.backend.model.dto.ChampionOutDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/api/champions")
@Slf4j
public class ChampionController {

    @Autowired
    private ChampionDAO dao;

    @Autowired
    private ChampionMapper mapper;

    @CrossOrigin
    @GetMapping
    public Collection<ChampionOutDto> findAll() {

        return mapper.championsToDtos(dao.findAll());
    }
}
