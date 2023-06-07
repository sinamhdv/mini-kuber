package minikuber.client;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum Command {
	GET_TASKS("get tasks"),
	GET_NODES("get nodes"),
	;

	private final String regex;

	private Command(String regex) {
		this.regex = regex;
	}

	public static Matcher getMatcher(String input, Command command) {
		return Pattern.compile(command.regex).matcher(input);
	}
}
