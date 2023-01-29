package edu.bbte.data.beim1992.backend.model.riot;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Metadata {

    private String matchId;
    private List<String> participants;
}
