package minikuber.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.regex.Matcher;

import minikuber.Main;
import minikuber.shared.LogType;
import minikuber.shared.Message;
import minikuber.shared.MessageType;
import minikuber.shared.Utils;

public class Client {
	private final Socket socket;
	private final DataInputStream sockin;
	private final DataOutputStream sockout;

	public Client(String host, int port) throws IOException {
		socket = new Socket(host, port);
		sockin = new DataInputStream(socket.getInputStream());
		sockout = new DataOutputStream(socket.getOutputStream());
		Utils.log(LogType.INFO, "Connected to server at " + host + ":" + port);
	}

	public void run() throws IOException {
		while (true) {
			System.out.print("[minikuber]> ");
			String command = Main.getScanner().nextLine();
			Matcher matcher = null;
			Message request = null;
			if ((matcher = Command.getMatcher(command, Command.GET_TASKS)) != null)
				request = new Message(MessageType.GET_TASKS, "");
			else if ((matcher = Command.getMatcher(command, Command.GET_NODES)) != null)
				request = new Message(MessageType.GET_NODES, "");
			else
				System.out.println("wrong command!");

			if (request != null) {
				sockout.writeUTF(request.toJson());
				Message response = Message.fromJson(sockin.readUTF());
				Utils.log(LogType.valueOf(response.getType().name()), response.getContent());
			}
		}
	}
}
