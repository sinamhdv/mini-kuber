package minikuber.server;

public class Task {
	private final String name;
	private final String intendedWorker;
	private String currentWorker = null;

	public Task(String name, String intendedWorker) {
		this.name = name;
		this.intendedWorker = intendedWorker;
	}

	public String getCurrentWorker() {
		return currentWorker;
	}

	public void setCurrentWorker(String currentWorker) {
		this.currentWorker = currentWorker;
	}

	public String getIntendedWorker() {
		return intendedWorker;
	}

	public String getName() {
		return name;
	}
}
