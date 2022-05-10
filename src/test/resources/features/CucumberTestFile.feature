Feature: Analytics information on political speeches can be retrieved

  Scenario Outline: Client makes call to GET using url
    When the client calls url "<urls>"
    Then the client receives status code based on "<Scenarios>"
    And the client receives information about the data "<urls>"

    Examples:
      | urls                                                                      | Scenarios                                   |
      | url=http://localhost:9095/fileHosting/politicalSpeech.csv                 | Valid url with one input url is passed     |


  Scenario Outline: Client makes call to GET using url
    When the client calls url with bad data"<urls>"
    Then the client receives bad status code based on "<Scenarios>"
    And the client receives message as response

    Examples:
      | urls                                                                          | Scenarios                                   |
      | url=http://localhost:9095/fileHosting/politicalSpeechInvalid.csv              | Valid url with invalid input passed     |