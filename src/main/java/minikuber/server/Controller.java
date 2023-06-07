package minikuber.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import minikuber.shared.Message;
import minikuber.shared.MessageType;

public class Controller {
	private static final HashMap<String, WorkerNode> workers = new HashMap<>();
	private static final Queue<Task> pendingTasks = new LinkedList<>();

	public synchronized static void addWorker(ClientHandler handler) {
		String name = "worker" + workers.size();
		handler.setNodeID(name);
		workers.put(name, new WorkerNode(handler));
		reschedule();
	}

	public synchronized static void removeWorker(String id) {
		WorkerNode worker = workers.get(id);
		for (Task task : worker.getActiveTasks())
			if (task.getIntendedWorker() == null)
				pendingTasks.add(task);
		workers.remove(id);
		reschedule();
	}

	private static ArrayList<Task> getAllTasks() {
		ArrayList<Task> allTasks = new ArrayList<>(pendingTasks);
		for (WorkerNode worker : workers.values()) {
			allTasks.addAll(worker.getActiveTasks());
			allTasks.addAll(worker.getPendingTasks());
		}
		return allTasks;
	}

	private static Task getTaskByName(String name) {
		ArrayList<Task> allTasks = getAllTasks();
		for (Task task : allTasks)
			if (task.getName().equals(name))
				return task;
		return null;
	}

	public synchronized static Message listTasks() {
		ArrayList<Task> allTasks = getAllTasks();
		if (allTasks.isEmpty())
			return new Message(MessageType.ERROR, "No task found");
		String result = "\nList of all tasks:\n";
		for (Task task : allTasks) {
			result += task.getName();
			if (task.getIntendedWorker() != null) result += " (target=" + task.getIntendedWorker() + ")";
			if (task.getCurrentWorker() != null) result += " (currentWorker=" + task.getCurrentWorker() + ")";
			else result += " (status=PENDING)";
			result += "\n";
		}
		return new Message(MessageType.OK, result);
	}

	public synchronized static Message listNodes() {
		if (workers.isEmpty())
			return new Message(MessageType.ERROR, "No worker in the cluster");
		String result = "\nList of worker nodes:\n";
		for (WorkerNode node : workers.values()) {
			result += node.getHandler().getNodeID() + " [" +
				node.getHandler().getSocket().getInetAddress() + ":" + node.getHandler().getSocket().getPort() + "] (" +
				(node.isActive() ? "ACTIVE" : "ASLEEP") + ")\n";
		}
		return new Message(MessageType.OK, result);
	}

	public synchronized static Message createTask(String requestData) {
		String name = requestData.split(" ")[0];
		String worker = null;
		if (requestData.contains(" ")) worker = requestData.split(" ")[1];
		if (getTaskByName(name) != null)
			return new Message(MessageType.ERROR, "This task already exists");
		if (worker != null && workers.get(worker) == null)
			return new Message(MessageType.ERROR, "No such worker found");
		Task task = new Task(name, worker);
		if (task.getIntendedWorker() == null) pendingTasks.add(task);
		else workers.get(task.getIntendedWorker()).getPendingTasks().add(task);
		reschedule();
		if (task.getCurrentWorker() != null)
			return new Message(MessageType.OK, "Task scheduled on " + task.getCurrentWorker());
		return new Message(MessageType.OK, "Task added to pending queue");
	}

	public synchronized static Message deleteTask(String name) {
		Task task = getTaskByName(name);
		if (task == null)
			return new Message(MessageType.ERROR, "No such task");
		for (WorkerNode worker : workers.values()) {
			worker.removeActiveTask(task);
			worker.getPendingTasks().remove(task);
		}
		pendingTasks.remove(task);
		reschedule();
		return new Message(MessageType.OK, "Success");
	}

	public synchronized static Message deactivate(String name) {
		return new Message(MessageType.OK, "");
	}

	public synchronized static Message activate(String name) {
		WorkerNode node = workers.get(name);
		if (node == null)
			return new Message(MessageType.ERROR, "No such worker");
		if (node.isActive())
			return new Message(MessageType.ERROR, "This node is already active");
		node.setActive(true);
		reschedule();
		return new Message(MessageType.OK, "Success");
	}

	private synchronized static void reschedule() {
		for (WorkerNode worker : workers.values()) {
			if (!worker.isActive()) continue;
			int capacity = worker.getHandler().getWorkerCapacity();
			while (worker.getActiveTasks().size() < capacity && !worker.getPendingTasks().isEmpty())
				worker.addActiveTask(worker.getPendingTasks().remove());
			while (worker.getActiveTasks().size() < capacity && !pendingTasks.isEmpty())
				worker.addActiveTask(pendingTasks.remove());
		}
	}
}
