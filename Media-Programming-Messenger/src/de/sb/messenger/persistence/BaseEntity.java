package de.sb.messenger.persistence;

import java.util.Collections;
import java.util.Set;

import javax.json.bind.annotation.JsonbProperty;
import javax.json.bind.annotation.JsonbTransient;
import javax.json.bind.annotation.JsonbVisibility;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;

import com.sun.xml.internal.txw2.annotation.XmlAttribute;

import javax.xml.bind.annotation.XmlAccessType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import de.sb.toolbox.bind.JsonProtectedPropertyStrategy;

@Entity
@Table(name="BaseEntity", schema="messenger")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "discriminator")
@JsonbVisibility(value = JsonProtectedPropertyStrategy.class)
@XmlAccessorType(XmlAccessType.NONE)
@XmlSeeAlso(value = { Person.class, Document.class, Message.class })
public abstract class BaseEntity implements Comparable<BaseEntity>{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long identity;

	@Positive
	@Column(nullable = false, updatable = true)
	private int version;

	@Column(nullable = false, updatable = false, insertable = true)
	@NotNull
	private long creationTimestamp;

	private Set<Message> messagesCaused;
	
	public BaseEntity() {
		super();
		this.creationTimestamp = System.currentTimeMillis();
		this.version = 1;
		this.messagesCaused = Collections.emptySet();
	}


	
	@JsonbProperty
	@XmlAttribute 
	public long getCreationTimestamp() {
		return creationTimestamp;
	}

	@JsonbProperty 
	@XmlAttribute
	public long getIdentity() {
		return identity;
	}

	@JsonbProperty 
	@XmlAttribute
	public int getVersion() {
		return version;
	}


	public void setVersion(int version) {
		this.version = version;
	}

	@Override
	public String toString() {
		return  this.getClass().getSimpleName() +"@" + identity;
	}
	
	@Override
	public int compareTo(BaseEntity o) {
		return Long.compare(this.identity, o.identity);
	}

	@JsonbTransient 
	@XmlTransient
	public Set<Message> getMessagesCaused() {
		return this.messagesCaused;
	}
	
}
