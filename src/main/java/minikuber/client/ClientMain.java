package minikuber.client;

public class ClientMain {
	private static final String SERVER_HOST = "127.0.0.1";
	private static final int SERVER_PORT = 12345;

	public static void run() throws Exception {
		System.out.println("======[minikuber client]======");
		new Client(SERVER_HOST, SERVER_PORT).run();
	}
}
