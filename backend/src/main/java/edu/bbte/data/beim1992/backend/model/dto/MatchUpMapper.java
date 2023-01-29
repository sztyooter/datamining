package edu.bbte.data.beim1992.backend.model.dto;

import edu.bbte.data.beim1992.backend.model.MatchUp;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;

import java.util.Collection;

@Mapper(componentModel = "spring")
public abstract class MatchUpMapper {

    public abstract MatchUpOutDto matchUpToDto(MatchUp matchUp);

    @IterableMapping(elementTargetType = MatchUpOutDto.class)
    public abstract Collection<MatchUpOutDto> matchUpsToDtos(Collection<MatchUp> matchUps);
}
