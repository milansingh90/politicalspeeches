package com.fashiondigital.PoliticalSpeeches.controller;

import com.fashiondigital.PoliticalSpeeches.exception.CustomErrorException;
import com.fashiondigital.PoliticalSpeeches.model.ApiError;
import com.fashiondigital.PoliticalSpeeches.model.ResponseDto;
import com.fashiondigital.PoliticalSpeeches.service.PoliticalSpeechService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/analysis/csv")
public class PoliticalSpeechController {

    @Autowired
    PoliticalSpeechService politicalSpeechService;

    @GetMapping
    public ResponseEntity  getInformationFromFile(@RequestParam List<String> url)  {
        try {
            ResponseDto responseDto = politicalSpeechService.parseInputFile(url);
            return ResponseEntity.ok(responseDto);
        }catch (CustomErrorException e){
            ApiError apiError = new ApiError( e.getMessage() );
            return new ResponseEntity<>( apiError,e.getStatus());
        }
    }
}
