package com.fashiondigital.PoliticalSpeeches.end2endTest;

import com.fashiondigital.PoliticalSpeeches.model.ApiError;
import com.fashiondigital.PoliticalSpeeches.model.ResponseDto;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Scope;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static io.cucumber.spring.CucumberTestContext.SCOPE_CUCUMBER_GLUE;

@Component
@Scope(SCOPE_CUCUMBER_GLUE)
public class PoliticialSpeechesClient {

    private final static String BASE_URI = "http://localhost";
    private final static String ANALYSIS_ENDPOINT = "/analysis/csv";

    private final static String WIREMOCK_PORT = System.getProperty("wiremock.port", "9096");

    @LocalServerPort
    private int port;

    WireMockServer wireMockServer = new WireMockServer(WireMockConfiguration.options().port(Integer.parseInt(WIREMOCK_PORT)));

    private final RestTemplate restTemplate = new RestTemplate();

    private String analysisEndpoint(String url) {
        return BASE_URI + ":" + port + ANALYSIS_ENDPOINT + "?" + url;
    }

    public ResponseEntity get(String url) throws InterruptedException {
        wireMockServer.start();
        mockCSVCall();
        ResponseEntity response = restTemplate.getForEntity(analysisEndpoint(url), ResponseDto.class);
        wireMockServer.stop();
        return response;
    }

    public void clean(String url) {
        restTemplate.delete(analysisEndpoint(url));
    }

    private void mockCSVCall() {
        WireMock.configureFor("localhost", wireMockServer.port());
        WireMock.stubFor(WireMock.get("/fileHosting/politicalSpeech.csv")
                .willReturn(WireMock.aResponse().withStatus(200)
                        .withHeader("Content-type", "text/plain; charset=utf-8")
                        .withBody("\"Speaker, Topic, Date, Words\"\n" +
                                "\"Alexander Abel, Education Policy,2012-10-30,5310\"\n" +
                                "\"Bernhard Belling, Coal Subsidies,2012-11-05,1210\"\n" +
                                "\"Caesare Collins, Coal Subsidies,2012-11-06,1119\"\n" +
                                "\"Alexander Abel, Internal Security,2012-12-11,911\"\n"+
                                "\"Caesare Collins, Education Policy,2013-10-30,5310\"\n")));
    }

    private void mockCSVCallWithInvalidData() {
        WireMock.configureFor("localhost", wireMockServer.port());
        WireMock.stubFor(WireMock.get("/fileHosting/politicalSpeechInvalid.csv")
                .willReturn(WireMock.aResponse().withStatus(200)
                        .withHeader("Content-type", "text/plain; charset=utf-8")
                        .withBody("\"Speaker, Topic, Date, Words\"\n" +
                                "\"Alexander Abel, Education Policy,2012-10-30,5310\"\n" +
                                "\"Bernhard Belling, Coal Subsidies,2012-11-05,1210\"\n" +
                                "\"Caesare Collins, Coal Subsidies,2012-11-06,1119\"\n" +
                                "\"Alexander Abel, Internal Security,2012-12-11,911\"\n"+
                                "\"Caesare Collins, Education Policy,2013-10-30,abcd\"\n")));
    }

    public ResponseEntity getBadData(String url) throws InterruptedException {
        wireMockServer.start();
        mockCSVCallWithInvalidData();
        ResponseEntity response;
        try{
            response = restTemplate.getForEntity(analysisEndpoint(url), ApiError.class);
        } catch(HttpClientErrorException e){
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        }
        wireMockServer.stop();
        return response;
    }

}
