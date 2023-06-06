package minikuber.shared;

public enum LogType {
	ERROR("[\033[31mERROR\033[0m]"),
	WARNING("[\033[33mWARNING\033[0m]"),
	INFO("[\033[36mINFO\033[0m]"),
	OK("[\033[32mOK\033[0m]");

	private final String outputSign;

	private LogType(String outputSign) {
		this.outputSign = outputSign;
	}

	public String getOutputSign() {
		return outputSign;
	}
}
