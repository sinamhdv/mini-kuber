package minikuber.shared;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {
	public static void log(LogType logType, String message) {
		System.out.println(
			logType.getOutputSign() + " [" +
			new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "] " +
			message
		);
	}
}
