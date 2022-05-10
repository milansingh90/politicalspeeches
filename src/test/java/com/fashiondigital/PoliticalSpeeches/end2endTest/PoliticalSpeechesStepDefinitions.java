package com.fashiondigital.PoliticalSpeeches.end2endTest;

import com.fashiondigital.PoliticalSpeeches.model.ApiError;
import com.fashiondigital.PoliticalSpeeches.model.ResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.assertj.core.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

public class PoliticalSpeechesStepDefinitions {

    ResponseEntity response;
    @Autowired
    private PoliticalSpeechesClient politicialSpeechesClient;

    @When("the client calls url {string}")
    public void the_client_calls_url(String url) throws Throwable  {
        response = politicialSpeechesClient.get(url);
    }

    @Then("the client receives status code based on {string}")
    public void the_client_receives_status_code_based_on(String statusCode) {
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK );
    }

    @And("the client receives information about the data {string}")
    public void the_client_receives_information_about_the_data(String responseData) {
        Assertions.assertThat(response.getBody()).isInstanceOf(ResponseDto.class);
    }

    @When("the client calls url with bad data{string}")
    public void the_client_calls_url_with_bad_data(String url) throws Throwable  {
        response = politicialSpeechesClient.getBadData(url);
    }
    @Then("the client receives bad status code based on {string}")
    public void the_client_receives_bad_status_code_based_on(String statusCode) {
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @And("the client receives message as response")
    public void the_client_receives_message_as_response() throws IOException {
        Assertions.assertThat(response.getBody()).isInstanceOf(String.class);
        ObjectMapper objectMapper = new ObjectMapper();
        ApiError apiError = objectMapper.readValue((String)response.getBody(), ApiError.class);
        Assertions.assertThat(apiError.getMessage()).isEqualTo("Invalid data in input file");
    }
}