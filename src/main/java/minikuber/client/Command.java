package minikuber.client;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum Command {
	GET_TASKS("get tasks"),
	GET_NODES("get nodes"),
	CREATE_TASK("create task --name=(?<name>\\w+)( --node=(?<node>\\w+))?"),
	DELETE_TASK("delete task --name=(?<name>\\w+)"),
	CORDON("cordon node (?<name>\\w+)"),
	UNCORDON("uncordon node (?<name>\\w+)"),
	;

	private final String regex;

	private Command(String regex) {
		this.regex = regex;
	}

	public static Matcher getMatcher(String input, Command command) {
		Matcher matcher = Pattern.compile(command.regex).matcher(input);
		return (matcher.matches() ? matcher : null);
	}
}
