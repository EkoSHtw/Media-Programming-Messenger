package entities;

import java.util.Date;
import java.util.List;

import javax.json.bind.annotation.JsonbProperty;
import javax.json.bind.annotation.JsonbVisibility;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

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
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.PositiveOrZero;
import de.sb.toolbox.bind.JsonProtectedPropertyStrategy;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "discriminator")
@JsonbVisibility(value = JsonProtectedPropertyStrategy.class)
@XmlAccessorType(XmlAccessType.NONE)
@XmlSeeAlso(value = { Person.class, Document.class, Message.class })
public class BaseEntity implements Comparable<BaseEntity>{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "IDENTITY_ID", nullable = false, updatable = false, unique = true)
	protected long identity;

	@NotEmpty
	@PositiveOrZero
	@Column(nullable = false, updatable = true)
	protected int version;

	@NotEmpty
	@PositiveOrZero
	@Column(nullable = false, updatable = false)
	protected long creationTimestamp;

	
	

	protected BaseEntity() {
		super();
		this.creationTimestamp = getCreationTimestamp();
		this.version = 1;
	}


	
	
	@JsonbProperty
	@XmlAttribute 
	public long getCreationTimestamp() {
		return creationTimestamp;
	}

	@JsonbProperty 
	@XmlID
	//@XmlJavaTypeAdapter(type=long.class,value=this.to)
	public long getIdentity() {
		return identity;
	}

	@Override
	public String toString() {
		return  this.getClass().getSimpleName() +"@" + identity;
	}
	
	@Override
	public int compareTo(BaseEntity o) {
		if(this.identity < o.getIdentity()) return 0;
		else return 1;
	}

	@JsonbProperty 
	public List<Message> getMessagesCaused() {
		// TODO Auto-generated method stub
		return null;
	}

	
	private long getCurrentDateTime() {
		Date date = new Date();
		long currentDateTime = date.getTime();
		return currentDateTime;
	}
}
