package entities;

import javax.validation.constraints.Size;

public class Message extends BaseEntity {
	
	@Size(min =1, max = 4093)
	private String message;
	
	private Person author;

	
	
	
	protected static void main(String[] arg) {
		//Message message = new Message("test", new Person());
	}
	
	public Message(long identity, int version, long creationTimestamp, @Size(min = 1, max = 4093) String message, Person author) {
		super(identity, version, creationTimestamp);
		this.message = message;
		this.author = author;
	}


	
	
	public String getMessage() {
		return message;
	}
	
	public Person getPerson() {
		return author;
	}
}
