package com.fashiondigital.PoliticalSpeeches.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/fileHosting")
public class FileHostingController {

    @GetMapping(value="/politicalSpeech.csv")
    public void getPoliticalSpeechesCsv(HttpServletResponse response) throws IOException {
        response.setContentType("text/plain; charset=utf-8");
        response.getWriter().print("\"Speaker, Topic, Date, Words\"\n" +
                "\"Alexander Abel,Education Policy,2012-10-30,5310\"\n" +
                "\"Bernhard Belling,Coal Subsidies,2012-11-05,1210\"\n" +
                "\"Caesare Collins,Coal Subsidies,2012-11-06,1119\"\n" +
                "\"Alexander Abel,Internal Security,2012-12-11,911\"\n");
    }

    @GetMapping(value="/politicalSpeechTest.csv")
    public void getPoliticalSpeechesCsvTest(HttpServletResponse response) throws IOException {
        response.setContentType("text/plain; charset=utf-8");
        response.getWriter().print("\"Speaker, Topic, Date, Words\"\n" +
                "\"Alexander Abel, Education Policy,2012-10-30,5310\"\n" +
                "\"Bernhard Belling, Coal Subsidies,2012-11-05,1210\"\n" +
                "\"Caesare Collins, Coal Subsidies,2012-11-06,1119\"\n" +
                "\"Alexander Abel, Internal Security,2012-12-11,911\"\n"+
                "\"Caesare Collins, Education Policy,2013-10-30,abcd\"\n");
    }
}
