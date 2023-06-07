package minikuber.client;

import minikuber.shared.ClientType;

public class ClientMain {
	private static final String SERVER_HOST = "127.0.0.1";
	private static final int SERVER_PORT = 12345;

	public static void run(ClientType clientType) throws Exception {
		System.out.println("======[minikuber client/worker]======");
		new Client(SERVER_HOST, SERVER_PORT, clientType).run();
	}
}
