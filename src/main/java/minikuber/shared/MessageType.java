package minikuber.shared;

public enum MessageType {
	// Request Types
	DECLARE_ROLE,

	// Client Commands
	GET_TASKS,
	GET_NODES,
	CREATE_TASK,
	DELETE_TASK,
	CORDON,
	UNCORDON,

	// Worker-Specific Types
	DECLARE_CAPACITY,
	PING,

	// Response Types
	OK,
	WARNING,
	ERROR,
}
