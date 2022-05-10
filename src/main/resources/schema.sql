DROP TABLE IF EXISTS PoliticalInformation;
create table PoliticalInformation
(
   speakerName varchar(255) not null,
   speakerTopic varchar(255) not null,
   dateOfSpeech date not null,
   numberOfWordsSpoken integer not null
);