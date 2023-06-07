package minikuber.server;

import java.util.ArrayList;

public class WorkerNode {
	private final String id;
	private final ArrayList<String> tasks = new ArrayList<>();
	private final ClientHandler handler;

	public WorkerNode(String id, ClientHandler handler) {
		this.id = id;
		this.handler = handler;
	}

	public ClientHandler getHandler() {
		return handler;
	}

	public String getId() {
		return id;
	}

	public ArrayList<String> getTasks() {
		return tasks;
	}
}
