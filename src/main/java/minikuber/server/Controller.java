package minikuber.server;

import java.util.HashMap;

import minikuber.shared.Message;
import minikuber.shared.MessageType;

public class Controller {
	private static final HashMap<String, WorkerNode> workers = new HashMap<>();

	public synchronized static void addWorker(ClientHandler handler) {
		String name = "worker" + workers.size();
		handler.setNodeID(name);
		workers.put(name, new WorkerNode(name, handler));
	}

	public synchronized static void removeWorker(String id) {
		workers.remove(id);
		// TODO: remove assigned tasks
	}

	public synchronized static Message getTasks() {
		return new Message(MessageType.OK, "");
	}

	public synchronized static Message getNodes() {
		if (workers.isEmpty())
			return new Message(MessageType.ERROR, "No worker in the cluster");
		String result = "\nList of worker nodes:\n";
		for (WorkerNode node : workers.values()) {
			result += node.getHandler().getNodeID() + " [" +
				node.getHandler().getSocket().getInetAddress() + ":" + node.getHandler().getSocket().getPort() + "] (" +
				(node.getHandler().isActive() ? "ACTIVE" : "ASLEEP") + ")\n";
		}
		return new Message(MessageType.OK, result);
	}

	public synchronized static Message createTask(String requestData) {
		String name = requestData.split(" ")[0];
		String worker = null;
		if (requestData.contains(" ")) worker = requestData.split(" ")[1];
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
