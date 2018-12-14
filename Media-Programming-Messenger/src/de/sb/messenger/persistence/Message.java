package de.sb.messenger.persistence;

import javax.json.bind.annotation.JsonbProperty;
import javax.json.bind.annotation.JsonbVisibility;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import de.sb.toolbox.bind.JsonProtectedPropertyStrategy;


@Entity
@Table(name="Message", schema="messenger")
@PrimaryKeyJoinColumn(name="messageIdentity")
@JsonbVisibility(value = JsonProtectedPropertyStrategy.class)
@XmlRootElement
@XmlType
public class Message extends BaseEntity {
	
	@Column(nullable = false, updatable = false, insertable = true)
	@Size(min =1, max = 4093)
	private String body;
<<<<<<< HEAD
	
	@ManyToOne
	@JoinColumn(name = "authorReference")
	private Person author;
	
	//TODO wofür ist das ? getter / setter?, datenbank refrence?
	@OneToOne
=======

	@ManyToOne(optional = false)
	@JoinColumn(name = "authorReference", nullable = false, updatable = false, insertable = true)
	private Person author;
	

	@ManyToOne(optional = false)
	@JoinColumn(name = "subjectReference", nullable = false, updatable = false, insertable = true)
>>>>>>> 9f786809f468b6788bfcfdcd365a4a0466abe101
	private BaseEntity subject;

	
	protected Message() {
		this(null, null, null);
	}
	
	public Message(String body, Person author, BaseEntity subject) {
		super();
		this.body = body;
		this.author = author;
		this.subject = subject;
	}

	@JsonbProperty
	@XmlAttribute
	protected long getAuthorReference() {
		return author.getIdentity();
		
	}
	
	@JsonbProperty
	@XmlAttribute
	protected long getSubjectReference() {
		return this.getIdentity();
	}
	
	@JsonbProperty
	@XmlAttribute
	public String getMessage() {
		return body;
	}
	
	@JsonbProperty
	@XmlAttribute
	public Person getPerson() {
		return author;
	}
}
