package minikuber.server;

public class ServerMain {
	public static void run() throws Exception {
		System.out.println("======[minikuber server]======");
		new Server(12345).serve();
	}
}
