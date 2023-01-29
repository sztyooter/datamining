package edu.bbte.data.beim1992.backend.model.dto;

import edu.bbte.data.beim1992.backend.model.Champion;
import java.util.ArrayList;
import java.util.Collection;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-01-07T15:12:03+0200",
    comments = "version: 1.5.3.Final, compiler: Eclipse JDT (IDE) 1.4.200.v20221012-0724, environment: Java 17.0.5 (Eclipse Adoptium)"
)
@Component
public class ChampionMapperImpl extends ChampionMapper {

    @Override
    public Champion dtoToChampion(ChampionInDto dto) {
        if ( dto == null ) {
            return null;
        }

        Champion champion = new Champion();

        champion.setName( dto.getName() );

        return champion;
    }

    @Override
    public ChampionOutDto championToDto(Champion champion) {
        if ( champion == null ) {
            return null;
        }

        ChampionOutDto championOutDto = new ChampionOutDto();

        championOutDto.setName( champion.getName() );

        return championOutDto;
    }

    @Override
    public Collection<ChampionOutDto> championsToDtos(Collection<Champion> champions) {
        if ( champions == null ) {
            return null;
        }

        Collection<ChampionOutDto> collection = new ArrayList<ChampionOutDto>( champions.size() );
        for ( Champion champion : champions ) {
            collection.add( championToDto( champion ) );
        }

        return collection;
    }
}
