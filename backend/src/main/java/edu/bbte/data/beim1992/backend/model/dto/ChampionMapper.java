package edu.bbte.data.beim1992.backend.model.dto;

import edu.bbte.data.beim1992.backend.model.Champion;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;

import java.util.Collection;

@Mapper(componentModel = "spring")
public abstract class ChampionMapper {

    public abstract Champion dtoToChampion(edu.bbte.data.beim1992.backend.model.dto.ChampionInDto dto);

    public abstract edu.bbte.data.beim1992.backend.model.dto.ChampionOutDto championToDto(Champion champion);

    @IterableMapping(elementTargetType = edu.bbte.data.beim1992.backend.model.dto.ChampionOutDto.class)
    public abstract Collection<edu.bbte.data.beim1992.backend.model.dto.ChampionOutDto> championsToDtos(Collection<Champion> champions);
}
