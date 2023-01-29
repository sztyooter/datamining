package edu.bbte.data.beim1992.backend.model.dto;

import edu.bbte.data.beim1992.backend.model.Match;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Mapper(componentModel = "spring")
public abstract class MatchMapper {

    public abstract Match dtoToMatch(MatchInDto dto);

    public abstract MatchOutDto matchToDto(Match match);

    @IterableMapping(elementTargetType = MatchOutDto.class)
    public abstract Collection<MatchOutDto> matchesToDtos(Collection<Match> matches);
}
