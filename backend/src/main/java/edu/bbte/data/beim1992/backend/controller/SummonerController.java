package edu.bbte.data.beim1992.backend.controller;

import edu.bbte.data.beim1992.backend.dao.SummonerDAO;
import edu.bbte.data.beim1992.backend.model.dto.SummonerMapper;
import edu.bbte.data.beim1992.backend.model.dto.SummonerOutDto;
import edu.bbte.data.beim1992.backend.utils.Property;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/api/summoners")
@Slf4j
public class SummonerController {

    @Autowired
    private SummonerDAO dao;

    @Autowired
    private SummonerMapper mapper;

    @GetMapping
    public Collection<SummonerOutDto> findAll() {

        return mapper.summonersToDtos(dao.findAll());
    }
}
