package minikuber.server;

import java.util.HashMap;

import minikuber.shared.Message;
import minikuber.shared.MessageType;

public class Controller {
	private static HashMap<String, ClientHandler> workers = new HashMap<>();

	public static HashMap<String, ClientHandler> getWorkers() {
		return workers;
	}

	public synchronized static void addWorker(ClientHandler handler) {
		String name = "worker" + workers.size();
		handler.setNodeID(name);
		workers.put(name, handler);
	}

	public synchronized static void removeWorker(String id) {
		workers.remove(id);
	}

	public synchronized static Message getTasks() {
		return new Message(MessageType.OK, "");
	}

	public synchronized static Message getNodes() {
		if (workers.isEmpty())
			return new Message(MessageType.ERROR, "No worker in the cluster");
		String result = "\nList of worker nodes:\n";
		for (ClientHandler node : workers.values()) {
			result += node.getNodeID() + " [" +
				node.getSocket().getInetAddress() + ":" + node.getSocket().getPort() + "] (" +
				(node.isActive() ? "ACTIVE" : "ASLEEP") + ")\n";
		}
		return new Message(MessageType.OK, result);
	}

	public synchronized static Message createTask(String requestData) {
		String name = requestData.split(" ")[0];
		String worker = null;
		if (requestData.contains(" ")) worker = requestData.split(" ")[1];
		return new Message(MessageType.OK, "");
	}

	public synchronized static Message deleteTask(String name) {
		return new Message(MessageType.OK, "");
	}

	public synchronized static Message deactivate(String name) {
		return new Message(MessageType.OK, "");
	}

	public synchronized static Message activate(String name) {
		return new Message(MessageType.OK, "");
	}
}
