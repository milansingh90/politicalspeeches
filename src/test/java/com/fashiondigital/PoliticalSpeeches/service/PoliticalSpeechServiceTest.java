package com.fashiondigital.PoliticalSpeeches.service;

import com.fashiondigital.PoliticalSpeeches.exception.CustomErrorException;
import com.fashiondigital.PoliticalSpeeches.model.PoliticalInformation;
import com.fashiondigital.PoliticalSpeeches.model.ResponseDto;
import com.fashiondigital.PoliticalSpeeches.repository.PoliticalInformationRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ActiveProfiles("unit-test")
@RunWith(SpringRunner.class)
public class PoliticalSpeechServiceTest {

    @Mock
    PoliticalInformationRepository politicalInformationRepository;

    @Mock
    RestTemplate restTemplate;

    @InjectMocks
    PoliticalSpeechService politicalSpeechService;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Before
    public void setUp() {

    }

    @Test
    public void successfullyReturnResponse(){
        List<PoliticalInformation> politicalInformationList = setUpPoliticalInformationTestData("2012-10-30",
                "1123", "Alex Abel", "Education Policy" );
        when(restTemplate.execute(any(), any(), any(), any(), (Object) any())).thenReturn(politicalInformationList);
        when(politicalInformationRepository.addAllInformation(any())).thenReturn(new int[1]);
        when(politicalInformationRepository.getSpeakerNameWhoSpokeLeastNumberOfWords()).thenReturn(Optional.of("Alex Abel"));
        when(politicalInformationRepository.getSpeakerNameWithCountOfMostSpeechesOnInternalSecurityTopic()).thenReturn(Optional.of("Alex Abel"));
        when(politicalInformationRepository.getSpeakerNameWithCountOfMostSpeeches()).thenReturn(Optional.of("Alex Abel"));

        ResponseDto responseDto = politicalSpeechService.parseInputFile(List.of("someUrl"));
        assertThat(responseDto.getMostSpeeches()).isEqualTo("Alex Abel");

    }

    @Test
    public void shouldReturnErrorForInvalidDate(){
        List<PoliticalInformation> politicalInformationList = setUpPoliticalInformationTestData("abhd-25-45",
                "1123", "Alex Abel", "Education Policy");
        when(restTemplate.execute(any(), any(), any(), any(), (Object) any())).thenReturn(politicalInformationList);

        exceptionRule.expect(CustomErrorException.class);
        exceptionRule.expectMessage("Invalid data in input file");

        politicalSpeechService.parseInputFile(List.of("someUrl"));

    }

    @Test
    public void shouldReturnErrorForInvalidSpeakerTopic(){
        List<PoliticalInformation> politicalInformationList = setUpPoliticalInformationTestData("2012-10-30",
                "1123", "Alex Abel", null);
        when(restTemplate.execute(any(), any(), any(), any(), (Object) any())).thenReturn(politicalInformationList);

        exceptionRule.expect(CustomErrorException.class);
        exceptionRule.expectMessage("Invalid data in input file");

        politicalSpeechService.parseInputFile(List.of("someUrl"));

    }

    @Test
    public void shouldReturnErrorForInvalidSpeakerName(){
        List<PoliticalInformation> politicalInformationList = setUpPoliticalInformationTestData("2012-10-30",
                "1123", null, "Education Policy");
        when(restTemplate.execute(any(), any(), any(), any(), (Object) any())).thenReturn(politicalInformationList);

        exceptionRule.expect(CustomErrorException.class);
        exceptionRule.expectMessage("Invalid data in input file");

        politicalSpeechService.parseInputFile(List.of("someUrl"));

    }

    @Test
    public void shouldReturnErrorForInvalidNumberOfWordsSpoken(){
        List<PoliticalInformation> politicalInformationList = setUpPoliticalInformationTestData("2012-10-30",
                "abcd", "Alex Abel", "Education Policy");
        when(restTemplate.execute(any(), any(), any(), any(), (Object) any())).thenReturn(politicalInformationList);

        exceptionRule.expect(CustomErrorException.class);
        exceptionRule.expectMessage("Invalid data in input file");

        politicalSpeechService.parseInputFile(List.of("someUrl"));

    }

    @Test
    public void shouldReturnErrorWhenFileContainsOnlyInvalidData(){
        List<PoliticalInformation> politicalInformationList = setUpPoliticalInformationTestData("2012-10-30",
                "513", "Alex Abel", "Education Policy");
        when(restTemplate.execute(any(), any(), any(), any(), (Object) any())).thenReturn(politicalInformationList);
        when(politicalInformationRepository.addAllInformation(any())).thenReturn(new int[0]);

        exceptionRule.expect(CustomErrorException.class);
        exceptionRule.expectMessage("Empty input file");

        politicalSpeechService.parseInputFile(List.of("someUrl"));

    }

    @Test
    public void shouldReturnErrorWhenInputFileIsEmpty(){
        when(restTemplate.execute(any(), any(), any(), any(), (Object) any())).thenReturn(null);

        exceptionRule.expect(CustomErrorException.class);
        exceptionRule.expectMessage("Input file is empty");

        politicalSpeechService.parseInputFile(List.of("someUrl"));

    }

    private List<PoliticalInformation> setUpPoliticalInformationTestData(
            String dateOfSpeech,
            String numberOfWordsSpoken,
            String speakerName,
            String speakerTopic
    ) {
        return List.of(PoliticalInformation.builder()
                .dateOfSpeech(dateOfSpeech)
                .numberOfWordsSpoken(numberOfWordsSpoken)
                .speakerName(speakerName)
                .speakerTopic(speakerTopic)
                .build());
    }
}
