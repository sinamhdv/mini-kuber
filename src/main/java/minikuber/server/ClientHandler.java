package minikuber.server;

import java.net.Socket;

import minikuber.shared.LogType;
import minikuber.shared.Utils;

public class ClientHandler extends Thread {
	private Socket socket;

	public ClientHandler(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		Utils.log(LogType.INFO, "New connection from " + socket.getInetAddress() + ":" + socket.getPort());
	}
}
