package io.github.shunshun94.FirstReplAIBot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.shunshun94.FirstReplAIBot.DTO.TalkingRequestBody;
import io.github.shunshun94.FirstReplAIBot.DTO.TalkingResponseBody;

public class ReplAIBotClientV1 {
	private final String BOT_ID;
	private final String KEY;
	private final String USER_ID_URL = "https://api.repl-ai.jp/v1/registration";
	private final String TALK_URL = "https://api.repl-ai.jp/v1/dialogue";
	public final static String NOMATCH = "NOMATCH";

	/**
	 * Constructor
	 * @param botId Bot ID of Repl-AI. See Repl-AI dashboard to get 
	 * @param key API Key of Repl-AI. See Repl-AI dashboard to get
	 */
	public ReplAIBotClientV1(String botId, String key) {
		BOT_ID = botId;
		KEY = key;
	}

	private JsonNode readRequestResult(HttpURLConnection con) throws IOException {
		try (
			InputStreamReader isr = new InputStreamReader(con.getInputStream());
			BufferedReader br = new BufferedReader(isr);
		) {
			String line;
			StringBuilder sb = new StringBuilder();
			while((line = br.readLine()) != null) {
				sb.append(line);
			}
			ObjectMapper mapper = new ObjectMapper();
			return mapper.readTree(sb.toString());
		}
	}

	/**
	 * Sending message to the scenario of the bot
	 * @param appUserId User ID given by getApplicationUserId method 
	 * @param voiceText Message for the scenario of the bot
	 * @return Response from the server including the bot's response message
	 * @throws IOException Communication with Repl-AI server is failed
	 */
	public TalkingResponseBody sendMessage(String appUserId, String voiceText) throws IOException {
		return sendMessage(appUserId, voiceText, false, "");
	}

	/**
	 * Sending initialize message to the scenario of the bot
	 * @param appUserId User ID given by getApplicationUserId method
	 * @param initTopicId Scenario ID. See Repl-AI dashboard to get
	 * @return Response from the server including the bot's first message
	 * @throws IOException Communication with Repl-AI server is failed
	 */
	public TalkingResponseBody sendInitMessage(String appUserId, String initTopicId) throws IOException {
		return sendMessage(appUserId, "init", true, initTopicId);
	}

	/**
	 * Sending message to the scenario of the bot
	 * @param appUserId User ID given by getApplicationUserId method
	 * @param voiceText Message for the scenario of the bot
	 * @param initTalkingFlag If this message is the initial message for the scenario of the bot, this value must be true. If not, this value must be false
	 * @param initTopicId Scenario ID. See Repl-AI dashboard to get. If initTalkingFlag is false, this should be empty String
	 * @return Response from the server including the bot's first message
	 * @throws IOException Communication with Repl-AI server is failed
	 */
	public TalkingResponseBody sendMessage(String appUserId, String voiceText, boolean initTalkingFlag, String initTopicId) throws IOException {
		TalkingResponseBody result = new TalkingResponseBody();
		TalkingRequestBody request = new TalkingRequestBody();
		request.appUserId = appUserId;
		request.botId = BOT_ID;
		request.voiceText = voiceText;
		request.initTalkingFlag = initTalkingFlag;
		request.initTopicId = initTopicId;

		HttpURLConnection con = null;
		try {
			URL url = new URL(TALK_URL);
			con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");
			con.setDoOutput(true);
			con.setRequestProperty("Content-Type", "application/JSON; charset=utf-8");
			con.setRequestProperty("x-api-key", KEY);
			try (
				OutputStreamWriter out = new OutputStreamWriter(con.getOutputStream());	
			) {
				out.write(request.toString());
			}
			int httpResult = con.getResponseCode();
			if(httpResult != HttpURLConnection.HTTP_OK) {
				throw new IOException("Request result is " + httpResult);
			}

			JsonNode parsedResult = readRequestResult(con);
			result.serverSendTime = parsedResult.get("serverSendTime").asText("");
			result.systemText.expression = parsedResult.get("systemText").get("expression").asText("");
			if(result.isNoMatch()) {
				result.systemText.utterance = "";
			} else {
				result.systemText.utterance = parsedResult.get("systemText").get("utterance").asText("");
			}
			return result;
		} catch (IOException e) {
			throw new IOException("Failed to send message", e);
		} finally {
			if(con != null) {
				con.disconnect();
			}
		}
	}

	/**
	 * Getthing User Id
	 * @return User Id as String
	 * @throws IOException Communication with Repl-AI server is failed
	 */
	public String getApplicationUserId() throws IOException {
		HttpURLConnection con = null;
		try {
			URL url = new URL(USER_ID_URL);
			con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");
			con.setDoOutput(true);
			con.setRequestProperty("Content-Type", "application/JSON; charset=utf-8");
			con.setRequestProperty("x-api-key", KEY);
			try (
				OutputStreamWriter out = new OutputStreamWriter(con.getOutputStream());	
			) {
				out.write(String.format("{\"botId\":\"%s\"}", BOT_ID));
			}
			int result = con.getResponseCode();
			if(result != HttpURLConnection.HTTP_OK) {
				throw new IOException("Request result is " + result);
			}
			JsonNode parsedResult = readRequestResult(con);
			return parsedResult.get("appUserId").asText();
		} catch (IOException e) {
			throw new IOException("Failed to get application user id", e);
		} finally {
			if(con != null) {
				con.disconnect();
			}
		}
	}
}


