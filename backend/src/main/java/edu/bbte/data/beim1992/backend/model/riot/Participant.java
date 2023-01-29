package edu.bbte.data.beim1992.backend.model.riot;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Participant {

    private String puuid;
    private String summonerName;
    private int teamId;
    private String teamPosition;
    private boolean win;
    private String championName;
}
