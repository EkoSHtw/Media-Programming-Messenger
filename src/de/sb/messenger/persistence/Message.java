package de.sb.messenger.persistence;

public class Message extends BaseEntity {
	
	private String message;
	private Person author;
	
	
	protected static void main(String[] arg) {
		Message message = new Message();
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
