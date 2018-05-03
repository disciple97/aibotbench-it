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
