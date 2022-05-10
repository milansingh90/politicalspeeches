package com.fashiondigital.PoliticalSpeeches;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(plugin = "pretty", features = "src/test/resources/features", glue =
        "com.fashiondigital.PoliticalSpeeches.end2endTest")
public class PoliticalSpeechesIntegrationTest {
}
