package io.github.shunshun94.FirstReplAIBot.DTO;

public class TalkingRequestBody {
	public String appUserId;
	public String botId;
	public String voiceText;
	public boolean initTalkingFlag;
	public String initTopicId;
	public String toString() {
		return String.format("{\"appUserId\":\"%s\", \"botId\":\"%s\", \"voiceText\":\"%s\", \"initTalkingFlag\":%s, \"initTopicId\":\"%s\"}", appUserId, botId, voiceText, initTalkingFlag, initTopicId);
	}
}
