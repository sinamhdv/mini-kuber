package minikuber.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import minikuber.shared.ClientType;
import minikuber.shared.LogType;
import minikuber.shared.Message;
import minikuber.shared.MessageType;
import minikuber.shared.Utils;

public class ClientHandler extends Thread {
	private final Socket socket;
	private DataInputStream sockin;
	private DataOutputStream sockout;
	private ClientType clientType;
	private int workerCapacity;
	private String nodeID;

	public ClientHandler(Socket socket) {
		this.socket = socket;
	}

	public void setNodeID(String nodeName) {
		this.nodeID = nodeName;
	}
	public String getNodeID() {
		return nodeID;
	}
	public Socket getSocket() {
		return socket;
	}
	public int getWorkerCapacity() {
		return workerCapacity;
	}

	@Override
	public void run() {
		try {
			Utils.log(LogType.INFO, "New connection from " + socket.getInetAddress() + ":" + socket.getPort());
			sockin = new DataInputStream(socket.getInputStream());
			sockout = new DataOutputStream(socket.getOutputStream());
			findClientType();
			if (clientType == ClientType.CLIENT)
				handleClient();
			else if (clientType == ClientType.WORKER)
				handleWorker();
			socket.close();
		} catch (IOException ex) {
			Utils.log(LogType.ERROR, "Lost connection with " + socket.getInetAddress() + ":" + socket.getPort());
		}
	}

	private void findClientType() throws IOException {
		Message message = Message.fromJson(sockin.readUTF());
		if (message.getType() == MessageType.DECLARE_ROLE) {
			clientType = ClientType.valueOf(message.getContent());
			chooseClientID();
			Utils.log(LogType.INFO,
				"[" + socket.getInetAddress() + ":" + socket.getPort() + "] declared role as " + clientType + " => " + nodeID);
		}
	}

	private void chooseClientID() {
		if (clientType == ClientType.WORKER) Controller.addWorker(this);
		else nodeID = socket.getInetAddress() + ":" + socket.getPort();
	}
	
	private void namedLog(LogType logType, String message) {
		Utils.log(logType, "[" + nodeID + "] " + message);
	}

	private void handleClient() throws IOException {
		while (true) {
			Message request = Message.fromJson(sockin.readUTF());
			Message response;
			switch (request.getType()) {
				case GET_NODES:
					response = Controller.listNodes();
					break;
				case GET_TASKS:
					response = Controller.listTasks();
					break;
				case CREATE_TASK:
					response = Controller.createTask(request.getContent());
					break;
				case DELETE_TASK:
					response = Controller.deleteTask(request.getContent());
					break;
				case CORDON:
					response = Controller.deactivate(request.getContent());
					break;
				case UNCORDON:
					response = Controller.activate(request.getContent());
					break;
				default:
					response = new Message(MessageType.ERROR, "wrong command!");
					break;
			}
			sockout.writeUTF(response.toJson());
		}
	}

	private void receiveWorkerCapacity() throws IOException {
		Message message = Message.fromJson(sockin.readUTF());
		if (message.getType() == MessageType.DECLARE_CAPACITY) {
			workerCapacity = Integer.parseInt(message.getContent());
			namedLog(LogType.INFO, "Declared capacity of " + workerCapacity);
		}
	}

	private void handleWorker() throws IOException {
		receiveWorkerCapacity();
		Controller.reschedule();
		socket.setSoTimeout(3000);
		while (true) {
			try {
				sockin.readUTF();
			} catch (IOException ex) {
				namedLog(LogType.ERROR, "Disconnected");
				Controller.removeWorker(nodeID);
				break;
			}
		}
	}

	public void sendMessage(Message message) {
		try {
			sockout.writeUTF(message.toJson());
		} catch (IOException ex) {}
	}
}
