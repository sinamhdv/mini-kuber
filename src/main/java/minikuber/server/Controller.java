package minikuber.server;

import minikuber.shared.Message;
import minikuber.shared.MessageType;

public class Controller {
	public static Message getTasks() {
		return new Message(MessageType.OK, "");
	}

	public static Message getNodes() {
		return new Message(MessageType.OK, "");
	}

	public static Message createTask(String requestData) {
		String name = requestData.split(" ")[0];
		String worker = null;
		if (requestData.contains(" ")) worker = requestData.split(" ")[1];
	}

	public static Message deleteTask(String name) {

	}

	public static Message deactivate(String name) {

	}

	public static Message activate(String name) {

	}
}
