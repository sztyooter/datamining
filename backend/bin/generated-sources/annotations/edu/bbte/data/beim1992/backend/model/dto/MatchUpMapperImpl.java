package edu.bbte.data.beim1992.backend.model.dto;

import edu.bbte.data.beim1992.backend.model.Champion;
import edu.bbte.data.beim1992.backend.model.MatchUp;
import java.util.ArrayList;
import java.util.Collection;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-01-07T15:12:02+0200",
    comments = "version: 1.5.3.Final, compiler: Eclipse JDT (IDE) 1.4.200.v20221012-0724, environment: Java 17.0.5 (Eclipse Adoptium)"
)
@Component
public class MatchUpMapperImpl extends MatchUpMapper {

    @Override
    public MatchUpOutDto matchUpToDto(MatchUp matchUp) {
        if ( matchUp == null ) {
            return null;
        }

        MatchUpOutDto matchUpOutDto = new MatchUpOutDto();

        matchUpOutDto.setAgainst( championToChampionOutDto( matchUp.getAgainst() ) );
        matchUpOutDto.setNumberOfGames( matchUp.getNumberOfGames() );
        matchUpOutDto.setNumberOfWins( matchUp.getNumberOfWins() );

        return matchUpOutDto;
    }

    @Override
    public Collection<MatchUpOutDto> matchUpsToDtos(Collection<MatchUp> matchUps) {
        if ( matchUps == null ) {
            return null;
        }

        Collection<MatchUpOutDto> collection = new ArrayList<MatchUpOutDto>( matchUps.size() );
        for ( MatchUp matchUp : matchUps ) {
            collection.add( matchUpToDto( matchUp ) );
        }

        return collection;
    }

    protected ChampionOutDto championToChampionOutDto(Champion champion) {
        if ( champion == null ) {
            return null;
        }

        ChampionOutDto championOutDto = new ChampionOutDto();

        championOutDto.setName( champion.getName() );

        return championOutDto;
    }
}
