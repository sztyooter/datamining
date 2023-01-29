package edu.bbte.data.beim1992.backend.model.riot;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.LinkedList;
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Info {

    private String gameMode;
    private String gameType;
    private LinkedList<Participant> participants;
    private LinkedList<Team> teams;
}
