package de.sb.messenger.persistence;

import javax.validation.constraints.Size;

public class Message extends BaseEntity {
	
	@Size(min =1, max = 4093)
	private String message;
	private Person author;

	
	
	public Message(@Size(min = 1, max = 4093) String message, Person author) {
		super(creationTimestamp, version, creationTimestamp);
		this.message = message;
		this.author = author;
	}

	protected static void main(String[] arg) {
		Message message = new Message("test", new Person());
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
