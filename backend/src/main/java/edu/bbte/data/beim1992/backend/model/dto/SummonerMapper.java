package edu.bbte.data.beim1992.backend.model.dto;

import edu.bbte.data.beim1992.backend.model.Summoner;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Mapper(componentModel = "spring")
public abstract class SummonerMapper {

    public abstract Summoner dtoToSummoner(SummonerInDto dto);

    public abstract SummonerOutDto summonerToDto(Summoner summoner);

    @IterableMapping(elementTargetType = SummonerOutDto.class)
    public abstract Collection<SummonerOutDto> summonersToDtos(Collection<Summoner> summoners);
}
