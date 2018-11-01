package entities;

import javax.json.bind.annotation.JsonbVisibility;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


@Entity
@Table(name="Messages", schema="messenger")
@PrimaryKeyJoinColumn(name="IDENTITY_ID")
@JsonbVisibility(value = JsonProtectedPropertyStrategy.class)
@XmlRootElement
@XmlType
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
	
	public Message(String body, Person author) {
		super();
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
