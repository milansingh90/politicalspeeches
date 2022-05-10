package com.fashiondigital.PoliticalSpeeches.service;

import com.fashiondigital.PoliticalSpeeches.exception.CustomErrorException;
import com.fashiondigital.PoliticalSpeeches.model.PoliticalInformation;
import com.fashiondigital.PoliticalSpeeches.model.ResponseDto;
import com.fashiondigital.PoliticalSpeeches.repository.PoliticalInformationRepository;
import com.fashiondigital.PoliticalSpeeches.validation.DateValidation;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.InputStreamReader;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PoliticalSpeechService {

    @Autowired
    PoliticalInformationRepository politicalInformationRepository;

    private final DateValidation dateValidation = new DateValidation();

    public ResponseDto parseInputFile(List<String> urlList) throws CustomErrorException {
        for(String url:urlList) {
            RestTemplate restTemplate = new RestTemplate();

            List<PoliticalInformation> politicalInformationList = restTemplate.execute(url, HttpMethod.GET, null, clientHttpResponse -> {
                InputStreamReader reader = new InputStreamReader(clientHttpResponse.getBody());
                CsvToBean<PoliticalInformation> csvToBean = new CsvToBeanBuilder<PoliticalInformation>(reader)
                        .withType(PoliticalInformation.class)
                        .withSeparator(',')
                        .withSkipLines(1)
                        .withIgnoreQuotations(true)
                        .build();
                return csvToBean.stream().collect(Collectors.toList());
            });

            int[] countInsertedRecords;
            if (politicalInformationList != null) {

                if(validateInputDataFromCsv(politicalInformationList)) {
                    countInsertedRecords = politicalInformationRepository.addAllInformation(politicalInformationList);
                    if(countInsertedRecords.length < 1)
                        throw new CustomErrorException(HttpStatus.BAD_REQUEST,"Empty input file");
                } else
                    throw new CustomErrorException(HttpStatus.BAD_REQUEST,"Invalid data in input file");
            }else
                throw new CustomErrorException(HttpStatus.BAD_REQUEST,"Input file is empty");
        }
        ResponseDto responseDto = new ResponseDto();

        Optional<String> speakerNameMostSpeeches = politicalInformationRepository.getSpeakerNameWithCountOfMostSpeeches();
        speakerNameMostSpeeches.ifPresent(responseDto::setMostSpeeches);
        Optional<String> speakerNameSpeakerTopics = politicalInformationRepository.getSpeakerNameWithCountOfMostSpeechesOnInternalSecurityTopic();
        speakerNameSpeakerTopics.ifPresent(responseDto::setMostSecurity);
        Optional<String> speakerNameLeastWords =politicalInformationRepository.getSpeakerNameWhoSpokeLeastNumberOfWords();
        speakerNameLeastWords.ifPresent(responseDto::setLeastWordy);

        return responseDto;
    }

    private boolean validateInputDataFromCsv(List<PoliticalInformation> politicalInformationList) {

        for (PoliticalInformation validateDateField : politicalInformationList) {

            if(StringUtils.isBlank(validateDateField.getSpeakerName()))
                return false;
            validateDateField.setSpeakerName(validateDateField.getSpeakerName().trim());

            if(StringUtils.isBlank(validateDateField.getSpeakerTopic()))
                return false;
            validateDateField.setSpeakerTopic(validateDateField.getSpeakerTopic().trim());

            if(StringUtils.isBlank(validateDateField.getDateOfSpeech()))
                return false;
            validateDateField.setDateOfSpeech(validateDateField.getDateOfSpeech().trim());
            if(!dateValidation.isValid(validateDateField.getDateOfSpeech()))
                return false;

            if(StringUtils.isBlank(validateDateField.getNumberOfWordsSpoken()))
                return false;
            validateDateField.setNumberOfWordsSpoken(validateDateField.getNumberOfWordsSpoken().trim());
            if (!StringUtils.isNumeric(validateDateField.getNumberOfWordsSpoken()))
                return false;
        }
        return true;
    }
}
