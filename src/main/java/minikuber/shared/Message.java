package minikuber.shared;

import com.google.gson.Gson;

public class Message {
	private final MessageAction action;
	private final String content;

	public Message(MessageAction action, String content) {
		this.action = action;
		this.content = content;
	}

	public MessageAction getAction() {
		return action;
	}

	public String getContent() {
		return content;
	}

	public String toJson() {
		return new Gson().toJson(this);
	}

	public static Message fromJson(String json) {
		return new Gson().fromJson(json, Message.class);
	}
}
