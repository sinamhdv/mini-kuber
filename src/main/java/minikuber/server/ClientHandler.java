package minikuber.server;

import java.io.IOException;
import java.net.Socket;

import minikuber.shared.LogType;
import minikuber.shared.Utils;

public class ClientHandler extends Thread {
	private final Socket socket;

	public ClientHandler(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		try {
			Utils.log(LogType.INFO, "New connection from " + socket.getInetAddress() + ":" + socket.getPort());
			socket.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}
