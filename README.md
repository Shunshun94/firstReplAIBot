# firstReplAIBot

Easy to use [Repl-AI](https://repl-ai.jp/) powered by NTT DOCOMO, Inc. and Intermedia Planning, Inc.

## Libraries

### jackson-databind

Repository: https://github.com/FasterXML/jackson-databind    
License: Apache License 2.0.

## Usage

### As Library

See Executor.java

```java
// Initialize Repl-AI Bot client
ReplAIBotClientV1 client = new ReplAIBotClientV1(botId, key);

// Getting Application User Id
String userId = client.getApplicationUserId();

// Initialize scenario
TalkingResponseBody initResponse = client.sendInitMessage(userId, scenarioId);
System.out.println(initResponse.systemText.expression);

// Sending chat message
TalkingResponseBody chatResponse = client.sendInitMessage(userId, "My first post");
System.out.println(initResponse.systemText.expression);
```


### As Simple chat client


```bash
$ mvn clean compile package
$ cd target
$ java -jar replAIBotClient-jar-with-dependencies.jar BOT_ID API_KEY SCENARIO_ID
```

Three arguments are found in Repl-AI console.
