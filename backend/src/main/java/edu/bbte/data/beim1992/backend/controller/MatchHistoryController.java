package edu.bbte.data.beim1992.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.bbte.data.beim1992.backend.dao.ChampionDAO;
import edu.bbte.data.beim1992.backend.dao.MatchDAO;
import edu.bbte.data.beim1992.backend.dao.SummonerDAO;
import edu.bbte.data.beim1992.backend.model.Champion;
import edu.bbte.data.beim1992.backend.model.Match;
import edu.bbte.data.beim1992.backend.model.Summoner;
import edu.bbte.data.beim1992.backend.model.dto.MatchMapper;
import edu.bbte.data.beim1992.backend.model.dto.MatchOutDto;
import edu.bbte.data.beim1992.backend.model.exceptions.ForbiddenException;
import edu.bbte.data.beim1992.backend.model.exceptions.NotFoundException;
import edu.bbte.data.beim1992.backend.model.exceptions.UnknownException;
import edu.bbte.data.beim1992.backend.model.riot.Participant;
import edu.bbte.data.beim1992.backend.utils.Property;
import edu.bbte.data.beim1992.backend.utils.HttpStatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/history")
@Slf4j
public class MatchHistoryController {

    @Autowired
    private Property property;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SummonerDAO summonerDAO;

    @Autowired
    private MatchDAO matchDAO;

    @Autowired
    private ChampionDAO championDAO;

    @Autowired
    private MatchMapper matchMapper;

    private int requests = 0;
    private int maxRequests = 99;
    private int waitTime = 2 * 60 * 1000;

    @GetMapping
    public Collection<MatchOutDto> getMatchHistory(@RequestParam(required = true) String name) {

        try {

            //get summoner by name
            Summoner summoner = getSummoner(name);

            //get summoner's match history
            LinkedList<String> matchIds = getMatches(summoner, 0, 20);

            LinkedList<Match> matches = new LinkedList<>();

            for (String matchId:matchIds) {

                matches.add(getMatch(matchId));
            }

            return matchMapper.matchesToDtos(matches);

        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void rateLimiting() throws InterruptedException {
        if (requests >= maxRequests) {

            log.info("Sleeping for " + waitTime / 1000 + " seconds because of reaching " + (maxRequests + 1) + " requests");
            Thread.sleep(waitTime);
            requests = 0;
        } else {

            if (requests % 20 == 0) {
                Thread.sleep(1000);
            }
            requests++;
        }
    }

    private Object getAnswerFromRiotApi(URL url, Class clazz) throws IOException, InterruptedException, NotFoundException {

        log.info("Sending request to: " + url);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestProperty("X-Riot-Token", property.getRiotAccessToken());

        InputStream responseStream = null;
        boolean success = false;
        int numberOfForbiddenAnswers = 0;

        while (success == false) {

            try {
                responseStream = connection.getInputStream();
                success = true;
            } catch (IOException exp) {

                switch (HttpStatusCode.getHttpStatusCode(connection.getResponseCode())) {

                    case FORBIDDEN:

                        if (numberOfForbiddenAnswers < 2) {
                            log.error("HttpResponse: " + HttpStatusCode.FORBIDDEN + "(" + HttpStatusCode.FORBIDDEN.getValue() + ") Sleeping for " + waitTime / 1000 + " seconds");
                            Thread.sleep(waitTime);
                            requests = 0;
                        }
                        else {
                            throw new ForbiddenException();
                        }

                        numberOfForbiddenAnswers++;
                        break;

                    case NOT_FOUND:

                        throw new NotFoundException();

                    case RATE_LIMIT_EXCEEDED:

                        int responseWaitTime = Integer.parseInt(connection.getHeaderField("Retry-After"));
                        waitTime += 500;
                        log.error("HttpResponse: " + HttpStatusCode.RATE_LIMIT_EXCEEDED + "(" + HttpStatusCode.RATE_LIMIT_EXCEEDED.getValue() + ") Sleeping for" + responseWaitTime + " seconds because of rate limiting");
                        Thread.sleep(responseWaitTime * 1000);
                        requests = 0;
                        break;

                    case SERVICE_UNAVAILABLE:

                        log.error("HttpResponse: " + HttpStatusCode.SERVICE_UNAVAILABLE + "(" + HttpStatusCode.SERVICE_UNAVAILABLE.getValue() + ") Sleeping for " + waitTime / 1000 + " seconds");
                        Thread.sleep(waitTime);
                        requests = 0;
                        break;

                    default:

                        log.error("Unknown status code: " + connection.getResponseCode());
                        throw new UnknownException();
                }
            }
        }

        return objectMapper.readValue(responseStream, clazz);
    }

    public Summoner getSummoner(String summonerName) throws InterruptedException, IOException, NotFoundException {

        log.info("Getting summoner with name: " + summonerName);

        // check if summoner is already known
        Summoner summoner = summonerDAO.findByName(summonerName);

        if (summoner == null) {

            rateLimiting();

            URL url = new URL("https://eun1.api.riotgames.com/lol/summoner/v4/summoners/by-name/" + URLEncoder.encode(summonerName, StandardCharsets.UTF_8));

            summoner = (Summoner) getAnswerFromRiotApi(url, Summoner.class);

            summonerDAO.create(summoner);
        }

        return summoner;
    }

    public LinkedList<String> getMatches(Summoner summoner, int start, int count) throws IOException, InterruptedException {

        rateLimiting();

        URL url = new URL("https://europe.api.riotgames.com/lol/match/v5/matches/by-puuid/" + summoner.getPuuid() + "/ids?start=" + start + "&count=" + count);

        return (LinkedList<String>) getAnswerFromRiotApi(url, LinkedList.class);
    }

    public Match getMatch(String matchId) throws IOException, InterruptedException {

        Match matchInDb = matchDAO.findByMatchId(matchId);

        if (matchInDb == null) {

            rateLimiting();

            URL url = new URL("https://europe.api.riotgames.com/lol/match/v5/matches/" + matchId);

            edu.bbte.data.beim1992.backend.model.riot.Match match = (edu.bbte.data.beim1992.backend.model.riot.Match) getAnswerFromRiotApi(url, edu.bbte.data.beim1992.backend.model.riot.Match.class);

            if (match.getInfo().getGameMode().equals("CLASSIC") && match.getMetadata().getParticipants().size() == 10) {

                log.info("SAVING GAME INTO DB");

                // saving the match into the database
                Match ret = new Match();

                ret.setMatchID(matchId);

                ConcurrentHashMap<String, String> roles = new ConcurrentHashMap<>();
                LinkedList<String> notPlayed = new LinkedList<>();

                for (Participant participant: match.getInfo().getParticipants()) {

                    Summoner summoner = summonerDAO.findByPuuid(participant.getPuuid());

                    if (summoner == null) {

                        //save summoner into database
                        summoner = new Summoner();

                        summoner.setPuuid(participant.getPuuid());
                        summoner.setName(participant.getSummonerName());

                        summonerDAO.create(summoner);

                        summoner = summonerDAO.findByPuuid(participant.getPuuid());
                    }

                    // save champion into database if it does not already exist
                    Champion champion = championDAO.findByName(participant.getChampionName());

                    if (champion == null) {

                        champion = new Champion();
                        champion.setName(participant.getChampionName());

                        championDAO.create(champion);

                        champion = championDAO.findByName(participant.getChampionName());
                    }

                    if (participant.isWin()) {

                        switch (participant.getTeamPosition()) {

                            case "TOP":

                                ret.setWinnerTop(champion);
                                roles.put(participant.getPuuid(), "winnerTop");
                                break;

                            case "JUNGLE":

                                ret.setWinnerJung(champion);
                                roles.put(participant.getPuuid(), "winnerJung");
                                break;

                            case "MIDDLE":

                                ret.setWinnerMid(champion);
                                roles.put(participant.getPuuid(), "winnerMid");
                                break;

                            case "BOTTOM":

                                ret.setWinnerBot(champion);
                                roles.put(participant.getPuuid(), "winnerBot");
                                break;

                            case "UTILITY":

                                ret.setWinnerSupp(champion);
                                roles.put(participant.getPuuid(), "winnerSupp");
                                break;

                            default:

                                notPlayed.add(participant.getPuuid());
                                break;
                        }
                    } else {

                        switch (participant.getTeamPosition()) {

                            case "TOP":

                                ret.setLoserTop(champion);
                                roles.put(participant.getPuuid(), "loserTop");
                                break;

                            case "JUNGLE":

                                ret.setLoserJung(champion);
                                roles.put(participant.getPuuid(), "loserJung");
                                break;

                            case "MIDDLE":

                                ret.setLoserMid(champion);
                                roles.put(participant.getPuuid(), "loserMid");
                                break;

                            case "BOTTOM":

                                ret.setLoserBot(champion);
                                roles.put(participant.getPuuid(), "loserBot");
                                break;

                            case "UTILITY":

                                ret.setLoserSupp(champion);
                                roles.put(participant.getPuuid(), "loserSupp");
                                break;

                            default:

                                notPlayed.add(participant.getPuuid());
                                break;
                        }
                    }
                }

                // checking the integrity of the match
                Champion missing = championDAO.findByName("");

                if (missing == null) {

                    missing = new Champion("");
                    championDAO.create(missing);
                    missing = championDAO.findByName("");
                }

                if (ret.getWinnerTop() == null || ret.getLoserTop() == null) {

                    ret.setWinnerTop(missing);
                    ret.setLoserTop(missing);
                }

                if (ret.getWinnerJung() == null || ret.getLoserJung() == null) {

                    ret.setWinnerJung(missing);
                    ret.setLoserJung(missing);
                }

                if (ret.getWinnerMid() == null || ret.getLoserMid() == null) {

                    ret.setWinnerMid(missing);
                    ret.setLoserMid(missing);
                }

                if (ret.getWinnerBot() == null || ret.getLoserBot() == null) {

                    ret.setWinnerBot(missing);
                    ret.setLoserBot(missing);
                }

                if (ret.getWinnerSupp() == null || ret.getLoserSupp() == null) {

                    ret.setWinnerSupp(missing);
                    ret.setLoserSupp(missing);
                }

                // saving match
                matchDAO.create(ret);
                ret = matchDAO.findByMatchId(matchId);

                // save the players and match connection
                for (Participant participant: match.getInfo().getParticipants()) {

                    boolean contains = false;

                    for (String puuid: notPlayed) {

                        if (puuid.equals(participant.getPuuid())) {

                            contains = true;
                            break;
                        }
                    }

                    if (contains == false) {

                        Summoner summoner = summonerDAO.findByPuuid(participant.getPuuid());
                        matchDAO.playedIn(ret, summoner, roles.get(participant.getPuuid()));
                    }
                }

                return ret;
            }
            else {

                log.info("NOT SAVING GAME: " + match.getInfo().getGameMode() + " " + match.getMetadata().getParticipants().size());
            }
        }
        else {

            log.info(matchInDb.getMatchID() + ": Match already in DB");
        }

        return matchInDb;
    }
}
