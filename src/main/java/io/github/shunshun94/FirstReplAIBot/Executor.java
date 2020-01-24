package io.github.shunshun94.FirstReplAIBot;

import java.io.IOException;
import java.util.Scanner;

import io.github.shunshun94.FirstReplAIBot.DTO.TalkingResponseBody;

public class Executor {

	public static void main(String[] args) {
		String botId = args[0].trim();
		String key = args[1].trim();
		String scenarioId = args[2].trim();
		ReplAIBotClientV1 client = new ReplAIBotClientV1(botId, key);
		try (
				Scanner scanner = new Scanner(System.in);	
		){
			String userId = client.getApplicationUserId();
			TalkingResponseBody initResponse = client.sendInitMessage(userId, scenarioId);
			System.out.println(String.format("Bot: %s", initResponse.systemText.expression));
			String voiceText = "";
			while(! voiceText.equals("exit")) {
				System.out.print("You: ");
				voiceText = scanner.nextLine();
				TalkingResponseBody chatResponse = client.sendMessage(userId, voiceText);
				if(! chatResponse.isNoMatch()) {
					System.out.println(String.format("Bot: %s", chatResponse.systemText.expression));
				}
			}
			System.out.println("Sys: bye");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
