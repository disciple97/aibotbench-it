# Table of Contents

* [External Services API](#external-services-api)
  * [NER](#ner)
  * [SQUAD](#squad)
* [AI Bot Bench Integration Tests](#ai-bot-bench-integration-tests)
  * [Running of Tests](#running-of-tests)
  * [Pattern Matching Integration Test](#pattern-matching-integration-test)

# External Services API

## NER

* Request body
  ```json
  {"text1":"What is the weather in Moscow?"}
  ```
  or
  ```json
  {"text1":"Какая погода в Санкт-Петербурге?"}
  ```

* Response body
  ```json
  [["What", "O"], ["is", "O"], ["the", "O"], ["weather", "O"], ["in", "O"], ["Moscow", "B-GPE"], ["?", "O"]]
  ```
  or
  ```json
  [["Какая", "O"], ["погода", "O"], ["в", "O"], ["Санкт-Петербурге", "B-LOC"], ["?", "O"]]
  ```   

## SQUAD

* Request body
  ```json
  {
    "context": "Some context should be provided here",
    "question": "Question is put here"
  }
  ```

* Response body
  ```json
  [ "Reply should be provided here", 0, 0.9782 ]
  ```
  The 1st item in the array is a reply itself, 2nd - position in a provided context, 3rd - confidence.

# AI Bot Bench Integration Tests

## Running of Tests

To run all integration tests simply execute the following command.
   
```bash
mvn verify
```

## Pattern Matching Integration Test

* After starting of this test you see the total number of recognized patterns to be matched.

  ```
  [INFO]: Total number of patters to be matched: <SOME NUMBER>
  ```

* Then you see input messages that are sent to AI API. They are printed out just to simplify tracking of a progress.

  ```
  [INFO]: Checking... <SOME MESSAGE 1>
  [INFO]: Checking... <SOME MESSAGE 2>
  ```

* After getting of all replies and matching of them to recognized patterns you see number of collected errors and success rate.

  ```
  [INFO]: Number of collected errors - <SOME NUMBER>, success rate - <SOME OTHER NUMBER>
  ```

* Finally if there were errors you would see a reply and unmatched pattern.

  ```
  Expecting:
   "<SOME REPLY>"
  to match pattern:
   "<SOME UNMATCHED PATTERN>"
  ```
