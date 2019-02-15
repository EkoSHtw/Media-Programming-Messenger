package de.sb.messenger.persistence;

import javax.json.bind.annotation.JsonbProperty;
import javax.json.bind.annotation.JsonbTransient;
import javax.json.bind.annotation.JsonbVisibility;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import de.sb.toolbox.bind.JsonProtectedPropertyStrategy;


@Entity
@Table(name="Message", schema="messenger")
@PrimaryKeyJoinColumn(name="messageIdentity")
@JsonbVisibility(value = JsonProtectedPropertyStrategy.class)
@XmlRootElement
@XmlType
public class Message extends BaseEntity {
	
	@Column(nullable = false, updatable = true)
	@Size(min =1, max = 4093)
	@NotNull
	private String body;

	@ManyToOne(optional = false)
	@JoinColumn(name = "authorReference", nullable = false, updatable = false, insertable = true)
	private Person author;
	

	@ManyToOne(optional = false)
	@JoinColumn(name = "subjectReference", nullable = false, updatable = false, insertable = true)
	private BaseEntity subject;

	
	protected Message() {
		this(null, null);
	}
	
	public Message(Person author, BaseEntity subject) {
		super();
		this.author = author;
		this.subject = subject;
	}

	
	@JsonbTransient
	@XmlTransient
	public Person getAuthor() {
		return author;
	}
	
	@JsonbProperty
	@XmlAttribute
	protected long getAuthorReference() {
		return this.author == null ? 0 : this.author.getIdentity();
	}
	

	@JsonbTransient
	@XmlTransient
	public BaseEntity getSubject() {
		return subject;
	}
	
	@JsonbProperty
	@XmlAttribute
	protected long getSubjectReference() {
		return this.subject == null ? 0 : this.subject.getIdentity();
	}

	@JsonbProperty
	@XmlAttribute
	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}	
}
