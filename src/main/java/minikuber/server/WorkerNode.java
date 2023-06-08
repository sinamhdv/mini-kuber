package minikuber.server;

import java.util.ArrayList;

import minikuber.shared.Message;
import minikuber.shared.MessageType;

public class WorkerNode {
	private final ArrayList<Task> activeTasks = new ArrayList<>();
	private ClientHandler handler;
	private boolean isActive = true;
	private final String id;

	public WorkerNode(ClientHandler handler) {
		this.handler = handler;
		this.id = handler.getNodeID();
	}

	public String getId() {
		return id;
	}
	
	public ClientHandler getHandler() {
		return handler;
	}

	public void disconnect() {
		handler = null;
		isActive = false;
		activeTasks.clear();
	}

	public ArrayList<Task> getActiveTasks() {
		return activeTasks;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
		try {
			handler.sendMessage(new Message(isActive ? MessageType.UNCORDON : MessageType.CORDON, ""));
		} catch (Exception ex) {}
	}

	public void addActiveTask(Task task) {
		activeTasks.add(task);
		task.setCurrentWorker(handler.getNodeID());
		handler.sendMessage(new Message(MessageType.CREATE_TASK, task.getName()));
	}

	public void removeActiveTask(Task task) {
		if (activeTasks.remove(task))
			handler.sendMessage(new Message(MessageType.DELETE_TASK, task.getName()));
	}
}
