package de.sb.messenger.persistence;

public class Message extends BaseEntity {
	
	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
