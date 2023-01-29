package edu.bbte.data.beim1992.backend;

import edu.bbte.data.beim1992.backend.controller.MatchHistoryController;
import edu.bbte.data.beim1992.backend.dao.MatchDAO;
import edu.bbte.data.beim1992.backend.dao.SummonerDAO;
import edu.bbte.data.beim1992.backend.model.Summoner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.LinkedList;

@SpringBootApplication
@Slf4j
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    private boolean getData = true;
    private int first = 0;
    private int count = 100;

    @Autowired
    private SummonerDAO summonerDAO;

    @Autowired
    private MatchHistoryController matchHistoryController;

    @Autowired
    private MatchDAO matchDAO;

    @Bean
    public CommandLineRunner runner(){

        return args -> {

            //matchDAO.updateMatchUps();

            if (getData) {

                LinkedList<Summoner> summoners = (LinkedList<Summoner>) summonerDAO.findAll();

                for (int i = first; i < first + count && i < summoners.size(); i++) {

                    matchHistoryController.getMatchHistory(summoners.get(i).getName());
                }

                log.info("All summoners checked");
            }
        };
    }
}
