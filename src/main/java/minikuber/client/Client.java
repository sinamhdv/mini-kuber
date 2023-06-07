package minikuber.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.regex.Matcher;

import minikuber.Main;
import minikuber.shared.ClientType;
import minikuber.shared.LogType;
import minikuber.shared.Message;
import minikuber.shared.MessageType;
import minikuber.shared.Utils;

public class Client {
	private final Socket socket;
	private final DataInputStream sockin;
	private final DataOutputStream sockout;
	private final ClientType clientType;
	private int workerCapacity;

	public Client(String host, int port, ClientType clientType) throws IOException {
		this.clientType = clientType;
		socket = new Socket(host, port);
		sockin = new DataInputStream(socket.getInputStream());
		sockout = new DataOutputStream(socket.getOutputStream());
		Utils.log(LogType.INFO, "Connected to server at " + host + ":" + port);
	}

	public void run() throws IOException {
		declareRole();
		if (clientType == ClientType.CLIENT) runClient();
		else runWorker();
	}

	private void runWorker() throws IOException {
		System.out.print("Enter capacity: ");
		try {
			workerCapacity = Integer.parseInt(Main.getScanner().nextLine());
		} catch (NumberFormatException ex) {
			System.out.println("Invalid number, using the default value of 4");
			workerCapacity = 4;
		}
		declareCapacity();
		new WorkerPing(sockout).start();
		while (true) {
			Message message = Message.fromJson(sockin.readUTF());
			switch (message.getType()) {
				case CREATE_TASK:
					Utils.log(LogType.OK, "New task added: " + message.getContent());
					break;
				case DELETE_TASK:
					Utils.log(LogType.OK, "Deleted task: " + message.getContent());
					break;
				case CORDON:
					Utils.log(LogType.INFO, "Worker deactivated");
					break;
				case UNCORDON:
					Utils.log(LogType.INFO, "Worker activated");
					break;
				default:
					break;
			}
		}
	}

	private void declareCapacity() throws IOException {
		sockout.writeUTF(new Message(MessageType.DECLARE_CAPACITY, Integer.toString(workerCapacity)).toJson());
	}

	private void runClient() throws IOException {
		while (true) {
			System.out.print("[minikuber]> ");
			String command = Main.getScanner().nextLine();
			Matcher matcher = null;
			Message request = null;
			if ((matcher = Command.getMatcher(command, Command.GET_TASKS)) != null)
				request = new Message(MessageType.GET_TASKS, "");
			else if ((matcher = Command.getMatcher(command, Command.GET_NODES)) != null)
				request = new Message(MessageType.GET_NODES, "");
			else if ((matcher = Command.getMatcher(command, Command.CREATE_TASK)) != null)
				request = new Message(MessageType.CREATE_TASK, matcher.group("name") +
					(matcher.group("node") == null ? "" : " " + matcher.group("node")));
			else if ((matcher = Command.getMatcher(command, Command.DELETE_TASK)) != null)
				request = new Message(MessageType.DELETE_TASK, matcher.group("name"));
			else if ((matcher = Command.getMatcher(command, Command.CORDON)) != null)
				request = new Message(MessageType.CORDON, matcher.group("name"));
			else if ((matcher = Command.getMatcher(command, Command.UNCORDON)) != null)
				request = new Message(MessageType.UNCORDON, matcher.group("name"));
			else
				System.out.println("wrong command!");

			if (request != null) {
				sockout.writeUTF(request.toJson());
				Message response = Message.fromJson(sockin.readUTF());
				Utils.log(LogType.valueOf(response.getType().name()), response.getContent());
			}
		}
	}

	private void declareRole() throws IOException {
		sockout.writeUTF(new Message(MessageType.DECLARE_ROLE, clientType.name()).toJson());
	}
}
