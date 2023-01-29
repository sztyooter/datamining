package edu.bbte.data.beim1992.backend.model.dto;

import edu.bbte.data.beim1992.backend.model.Champion;
import edu.bbte.data.beim1992.backend.model.Match;
import java.util.ArrayList;
import java.util.Collection;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-01-08T00:37:37+0200",
    comments = "version: 1.5.3.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-7.4.jar, environment: Java 17.0.3 (Amazon.com Inc.)"
)
@Component
public class MatchMapperImpl extends MatchMapper {

    @Override
    public Match dtoToMatch(MatchInDto dto) {
        if ( dto == null ) {
            return null;
        }

        Match match = new Match();

        match.setMatchID( dto.getMatchID() );
        match.setWinnerTop( championInDtoToChampion( dto.getWinnerTop() ) );
        match.setWinnerJung( championInDtoToChampion( dto.getWinnerJung() ) );
        match.setWinnerMid( championInDtoToChampion( dto.getWinnerMid() ) );
        match.setWinnerBot( championInDtoToChampion( dto.getWinnerBot() ) );
        match.setWinnerSupp( championInDtoToChampion( dto.getWinnerSupp() ) );
        match.setLoserTop( championInDtoToChampion( dto.getLoserTop() ) );
        match.setLoserJung( championInDtoToChampion( dto.getLoserJung() ) );
        match.setLoserMid( championInDtoToChampion( dto.getLoserMid() ) );
        match.setLoserBot( championInDtoToChampion( dto.getLoserBot() ) );
        match.setLoserSupp( championInDtoToChampion( dto.getLoserSupp() ) );

        return match;
    }

    @Override
    public MatchOutDto matchToDto(Match match) {
        if ( match == null ) {
            return null;
        }

        MatchOutDto matchOutDto = new MatchOutDto();

        matchOutDto.setMatchID( match.getMatchID() );
        matchOutDto.setWinnerTop( championToChampionOutDto( match.getWinnerTop() ) );
        matchOutDto.setWinnerJung( championToChampionOutDto( match.getWinnerJung() ) );
        matchOutDto.setWinnerMid( championToChampionOutDto( match.getWinnerMid() ) );
        matchOutDto.setWinnerBot( championToChampionOutDto( match.getWinnerBot() ) );
        matchOutDto.setWinnerSupp( championToChampionOutDto( match.getWinnerSupp() ) );
        matchOutDto.setLoserTop( championToChampionOutDto( match.getLoserTop() ) );
        matchOutDto.setLoserJung( championToChampionOutDto( match.getLoserJung() ) );
        matchOutDto.setLoserMid( championToChampionOutDto( match.getLoserMid() ) );
        matchOutDto.setLoserBot( championToChampionOutDto( match.getLoserBot() ) );
        matchOutDto.setLoserSupp( championToChampionOutDto( match.getLoserSupp() ) );

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
