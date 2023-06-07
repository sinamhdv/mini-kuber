package minikuber.shared;

import com.google.gson.Gson;

public class Message {
	private final MessageType type;
	private final String content;

	public Message(MessageType type, String content) {
		this.type = type;
		this.content = content;
	}

	public MessageType getType() {
		return type;
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
