package com.fashiondigital.PoliticalSpeeches.repository;

import com.fashiondigital.PoliticalSpeeches.model.PoliticalInformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class PoliticalInformationRepository {
    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);

    }


    public Optional<String> getSpeakerNameWithCountOfMostSpeeches() {
        final SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("year", 2013);
        try{
            String name= namedParameterJdbcTemplate.queryForObject("SELECT speakerName FROM PoliticalInformation " +
                    " WHERE YEAR(dateOfSpeech) = :year" +
                    " GROUP BY speakerName " +
                    " ORDER BY COUNT(speakerName) DESC " +
                    " LIMIT 1", namedParameters, String.class);
            return Optional.ofNullable(name);
        }catch (DataAccessException e){
            return Optional.empty();
        }

    }

    public Optional<String> getSpeakerNameWithCountOfMostSpeechesOnInternalSecurityTopic() {
        final SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("speakerTopic", "Internal Security");
        try{
            String name= namedParameterJdbcTemplate.queryForObject("SELECT speakerName FROM PoliticalInformation " +
                    " WHERE speakerTopic = :speakerTopic " +
                    " GROUP BY speakerName, speakerTopic " +
                    " ORDER BY COUNT(speakerName) DESC " +
                    " LIMIT 1", namedParameters, String.class);
            return Optional.ofNullable(name);
        }catch (DataAccessException e){
            return Optional.empty();
        }

    }

    public Optional<String> getSpeakerNameWhoSpokeLeastNumberOfWords() {
        final SqlParameterSource namedParameters = new MapSqlParameterSource();
        try {
            String name = namedParameterJdbcTemplate.queryForObject("SELECT speakerName FROM PoliticalInformation " +
                    " ORDER BY numberOfWordsSpoken " +
                    " LIMIT 1", namedParameters, String.class);
            return Optional.ofNullable(name);
        } catch (DataAccessException e){
            return Optional.empty();
        }
    }

    public int[] addAllInformation(List<PoliticalInformation> politicalInformationList){
        List<MapSqlParameterSource> params = new ArrayList<>();

        for (PoliticalInformation batchAdd : politicalInformationList) {
            MapSqlParameterSource source = new MapSqlParameterSource();
            source.addValue("speakerName", batchAdd.getSpeakerName().trim());
            source.addValue("speakerTopic", batchAdd.getSpeakerTopic().trim());
            LocalDate localDate = LocalDate.parse(batchAdd.getDateOfSpeech(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            source.addValue("dateOfSpeech", localDate);
            source.addValue("numberOfWordsSpoken", batchAdd.getNumberOfWordsSpoken());
            params.add(source);
        }

        try{
            String sql = "INSERT INTO PoliticalInformation VALUES (:speakerName, :speakerTopic, :dateOfSpeech, :numberOfWordsSpoken)";
            return namedParameterJdbcTemplate.batchUpdate(sql, params.toArray(MapSqlParameterSource[]::new));
        }catch (Exception e){
            return new int[]{0};
        }
    }
}
