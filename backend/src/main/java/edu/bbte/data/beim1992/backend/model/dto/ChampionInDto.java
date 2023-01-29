package edu.bbte.data.beim1992.backend.model.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class ChampionInDto {

    @NotNull
    @Size(max = 255)
    private String name;
}
