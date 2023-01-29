package edu.bbte.data.beim1992.backend.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class PropertyFactory {

    private String riotAccessToken = "RGAPI-adca2b1f-5b4b-4743-a198-67b139092335";
    private int minimumGames = 20;

    @Bean
    public Property getProperty() {
        Property properties = new Property();

        properties.setRiotAccessToken(riotAccessToken);
        properties.setMinimumGames(minimumGames);

        return properties;
    }
}
