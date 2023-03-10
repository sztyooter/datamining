package edu.bbte.data.beim1992.backend.model.dto;

import edu.bbte.data.beim1992.backend.model.Summoner;
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
public class SummonerMapperImpl extends SummonerMapper {

    @Override
    public Summoner dtoToSummoner(SummonerInDto dto) {
        if ( dto == null ) {
            return null;
        }

        Summoner summoner = new Summoner();

        summoner.setName( dto.getName() );
        summoner.setPuuid( dto.getPuuid() );

        return summoner;
    }

    @Override
    public SummonerOutDto summonerToDto(Summoner summoner) {
        if ( summoner == null ) {
            return null;
        }

        SummonerOutDto summonerOutDto = new SummonerOutDto();

        summonerOutDto.setName( summoner.getName() );
        summonerOutDto.setPuuid( summoner.getPuuid() );

        return summonerOutDto;
    }

    @Override
    public Collection<SummonerOutDto> summonersToDtos(Collection<Summoner> summoners) {
        if ( summoners == null ) {
            return null;
        }

        Collection<SummonerOutDto> collection = new ArrayList<SummonerOutDto>( summoners.size() );
        for ( Summoner summoner : summoners ) {
            collection.add( summonerToDto( summoner ) );
        }

        return collection;
    }
}
