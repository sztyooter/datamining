package edu.bbte.data.beim1992.backend.model.riot;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Match {

    private Info info;
    private Metadata metadata;
}
