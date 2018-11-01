package entities;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Entity
@Table(name="Messages", schema="messenger")
@PrimaryKeyJoinColumn(name="IDENTITY_ID")
public class Message extends BaseEntity {
	
	@Size(min =1, max = 4093)
	private String body;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name = "IDENTITY_ID")
	private Person author;

	
	protected Message() {
		//Message message = new Message("test", new Person());
	}
	
	public Message(long identity, int version, long creationTimestamp, String body, Person author) {
		super(identity, version, creationTimestamp);
		this.body = body;
		this.author = author;
	}


	protected long getAuthorReference() {
		return author.getIdentity();
		
	}
	
	protected long getSubjectReference() {
		return this.getIdentity();
	}
	
	public String getMessage() {
		return body;
	}
	
	public Person getPerson() {
		return author;
	}
}
