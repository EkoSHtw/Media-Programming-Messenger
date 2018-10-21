package entities;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Entity
public class Message extends BaseEntity {
	
	@Size(min =1, max = 4093)
	private String body;
	
	@NotNull
	@ManyToOne
	@PrimaryKeyJoinColumn
	private Person author;

	
	
	
	protected Message() {
		//Message message = new Message("test", new Person());
	}
	
	public Message(long identity, int version, long creationTimestamp, String body, Person author) {
		super(identity, version, creationTimestamp);
		this.body = body;
		this.author = author;
	}


	
	
	public String getMessage() {
		return body;
	}
	
	public Person getPerson() {
		return author;
	}
}
