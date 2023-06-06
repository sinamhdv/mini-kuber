package minikuber.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import minikuber.shared.LogType;
import minikuber.shared.Utils;

public class Server {
	private final int port;
	private final ServerSocket serverSocket;

	public Server(int port) throws IOException {
		this.port = port;
		serverSocket = new ServerSocket(port);
	}

	public void serve() throws IOException {
		Utils.log(LogType.INFO, "Listening on port " + port + "...");
		while (true) {
			Socket clientSocket = serverSocket.accept();
			new ClientHandler(clientSocket).start();
		}
	}
}
