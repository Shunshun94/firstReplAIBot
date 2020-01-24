package io.github.shunshun94.FirstReplAIBot.DTO;

public class TalkingResponseBody {
	public SystemText systemText;
	public String serverSendTime;
	public final static String NOMATCH = "NOMATCH";
	public TalkingResponseBody() {
		systemText = new SystemText();
	}
	/**
	 * Confirming the bot returns nothing
	 * @return If the bot returns nothing, true. If the bot returns some messages, false
	 */
	public boolean isNoMatch() {
		return systemText.expression.equals(NOMATCH);
	}
}