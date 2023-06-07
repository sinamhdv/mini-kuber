package minikuber;

import java.util.Scanner;

import minikuber.client.ClientMain;
import minikuber.server.ServerMain;
import minikuber.shared.ClientType;

public class Main {
	private static Scanner scanner = new Scanner(System.in);

	public static Scanner getScanner() {
		return scanner;
	}

	public static void main(String[] args) throws Exception {
		System.out.println("Please choose a role:");
		System.out.println("1) server");
		System.out.println("2) client");
		System.out.println("3) worker");
		System.out.print("> ");
		String role = scanner.nextLine();
		switch (role) {
			case "1":
				ServerMain.run();
				break;
			case "2":
				ClientMain.run(ClientType.CLIENT);
				break;
			case "3":
				ClientMain.run(ClientType.WORKER);
				break;
			default:
				System.out.println("Error: Invalid role");
		}
		scanner.close();
	}
}
