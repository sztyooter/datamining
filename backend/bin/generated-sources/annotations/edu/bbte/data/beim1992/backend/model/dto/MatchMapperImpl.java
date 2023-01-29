package edu.bbte.data.beim1992.backend.model.dto;

import edu.bbte.data.beim1992.backend.model.Champion;
import edu.bbte.data.beim1992.backend.model.Match;
import java.util.ArrayList;
import java.util.Collection;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-01-29T18:37:06+0200",
    comments = "version: 1.5.3.Final, compiler: Eclipse JDT (IDE) 3.33.0.v20221215-1352, environment: Java 17.0.5 (Eclipse Adoptium)"
)
@Component
public class MatchMapperImpl extends MatchMapper {

    @Override
    public Match dtoToMatch(MatchInDto dto) {
        if ( dto == null ) {
            return null;
        }

        Match match = new Match();

        match.setLoserBot( championInDtoToChampion( dto.getLoserBot() ) );
        match.setLoserJung( championInDtoToChampion( dto.getLoserJung() ) );
        match.setLoserMid( championInDtoToChampion( dto.getLoserMid() ) );
        match.setLoserSupp( championInDtoToChampion( dto.getLoserSupp() ) );
        match.setLoserTop( championInDtoToChampion( dto.getLoserTop() ) );
        match.setMatchID( dto.getMatchID() );
        match.setWinnerBot( championInDtoToChampion( dto.getWinnerBot() ) );
        match.setWinnerJung( championInDtoToChampion( dto.getWinnerJung() ) );
        match.setWinnerMid( championInDtoToChampion( dto.getWinnerMid() ) );
        match.setWinnerSupp( championInDtoToChampion( dto.getWinnerSupp() ) );
        match.setWinnerTop( championInDtoToChampion( dto.getWinnerTop() ) );

        return match;
    }

    @Override
    public MatchOutDto matchToDto(Match match) {
        if ( match == null ) {
            return null;
        }

        MatchOutDto matchOutDto = new MatchOutDto();

        matchOutDto.setLoserBot( championToChampionOutDto( match.getLoserBot() ) );
        matchOutDto.setLoserJung( championToChampionOutDto( match.getLoserJung() ) );
        matchOutDto.setLoserMid( championToChampionOutDto( match.getLoserMid() ) );
        matchOutDto.setLoserSupp( championToChampionOutDto( match.getLoserSupp() ) );
        matchOutDto.setLoserTop( championToChampionOutDto( match.getLoserTop() ) );
        matchOutDto.setMatchID( match.getMatchID() );
        matchOutDto.setWinnerBot( championToChampionOutDto( match.getWinnerBot() ) );
        matchOutDto.setWinnerJung( championToChampionOutDto( match.getWinnerJung() ) );
        matchOutDto.setWinnerMid( championToChampionOutDto( match.getWinnerMid() ) );
        matchOutDto.setWinnerSupp( championToChampionOutDto( match.getWinnerSupp() ) );
        matchOutDto.setWinnerTop( championToChampionOutDto( match.getWinnerTop() ) );

        return matchOutDto;
    }

    @Override
    public Collection<MatchOutDto> matchesToDtos(Collection<Match> matches) {
        if ( matches == null ) {
            return null;
        }

        Collection<MatchOutDto> collection = new ArrayList<MatchOutDto>( matches.size() );
        for ( Match match : matches ) {
            collection.add( matchToDto( match ) );
        }

        return collection;
    }

    protected Champion championInDtoToChampion(ChampionInDto championInDto) {
        if ( championInDto == null ) {
            return null;
        }

        Champion champion = new Champion();

        champion.setName( championInDto.getName() );

        return champion;
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
