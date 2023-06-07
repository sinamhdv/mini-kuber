package minikuber.server;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import minikuber.shared.Message;
import minikuber.shared.MessageType;

public class WorkerNode {
	private final ArrayList<Task> activeTasks = new ArrayList<>();
	private final ClientHandler handler;
	private boolean isActive = true;
	private final Queue<Task> pendingTasks = new LinkedList<>();

	public WorkerNode(ClientHandler handler) {
		this.handler = handler;
	}

	public ClientHandler getHandler() {
		return handler;
	}

	public ArrayList<Task> getActiveTasks() {
		return activeTasks;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public Queue<Task> getPendingTasks() {
		return pendingTasks;
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
