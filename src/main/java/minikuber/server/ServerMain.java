package minikuber.server;

public class ServerMain {
	private static final int SERVER_PORT = 12345;

	public static void run() throws Exception {
		System.out.println("======[minikuber server]======");
		new Server(SERVER_PORT).serve();
	}
}
