package minikuber.client;

import java.io.DataOutputStream;

import minikuber.shared.Message;
import minikuber.shared.MessageType;

public class WorkerPing extends Thread {
	private final DataOutputStream sockout;

	public WorkerPing(DataOutputStream sockout) {
		this.sockout = sockout;
		this.setDaemon(true);
	}

	@Override
	public void run() {
		try {
			while (true) {
				this.sockout.writeUTF(new Message(MessageType.PING, "").toJson());
				Thread.sleep(1000);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
