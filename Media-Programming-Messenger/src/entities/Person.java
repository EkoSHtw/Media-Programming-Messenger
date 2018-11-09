package entities;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.json.bind.annotation.JsonbProperty;
import javax.json.bind.annotation.JsonbTransient;
import javax.json.bind.annotation.JsonbVisibility;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.sun.xml.internal.txw2.annotation.XmlAttribute;

import de.sb.toolbox.bind.JsonProtectedPropertyStrategy;

@Entity
@Table(name="People", schema="messenger")
@PrimaryKeyJoinColumn(name="IDENTITY_ID")
@JsonbVisibility(value = JsonProtectedPropertyStrategy.class)
@XmlRootElement
@XmlType
public class Person extends BaseEntity {
	
	@NotNull
	@Embedded
	private Name name;
	
	@NotNull
	@Embedded
	private Address address;
	
	@NotNull
	@Email
	@Size(min = 3, max = 128)
	@Column(unique=true)
	private String email;
	
	@NotNull
	@Size(min = 32, max = 32)
	private byte[] password;
	
	@NotNull
	@OneToOne
	private Document avatar;
	
	@NotNull
	@OneToMany
	@JoinColumn
	private Set<Message> messages;
	
	@NotNull
	@Enumerated
	private Group group;
	
	@NotNull
	@ManyToMany(mappedBy = "peopleObserved")
	private Set<Person> peopleObserving;

	@NotNull
	@ManyToMany
	@JoinTable
	private Set<Person> peopleObserved;
	
	
	
	protected Person() {
		this(null);
	}
	
	//TODO wieso avatar übergeben? und keine mail ? wie wird das alles gesetzt beim anmelden ?
	public Person( Document avatar) {
		
		super();
		
		this.name = new Name();
		this.email = "";
		this.password = HashTools.sha256HashCode("");
		this.avatar = avatar;
		this.messages = Collections.emptySet();
		this.group = Group.USER;
		this.peopleObserving = Collections.emptySet();
		this.peopleObserved = new HashSet<>();
		this.address = new Address();
	}
	
	

	protected long[] getPeopleObservingReference() {
		long[] observingIds = new long[peopleObserving.size()];
		int i =0;
		for(Person p: peopleObserving) {
			observingIds[i] = p.identity;
			i++;
		}
		
		return observingIds;
	}
	
	protected long getAvatarReference(){
		return avatar.getIdentity();
	}
	

	protected long[] getPeopleObservedReferences(){
		long[] observedIds = new long[peopleObserved.size()];
		int i =0;
		for(Person p: peopleObserved) {
			observedIds[i] = p.identity;
			i++;
		}
		return observedIds;
	}
	
	

	
	@JsonbProperty 
	@XmlAttribute 
	public Name getName() {
		return name;
	}
	
	
	@JsonbTransient
	public void setName(Name name) {
		this.name = name;
	}

	@JsonbProperty
	@XmlAttribute 
	public String getEmail() {
		return email;
	}

	@JsonbTransient 
	public void setEmail(String email) {
		this.email = email;
	}

	@JsonbProperty 
	@XmlAttribute 
	public byte[] getPasswordHash() {
		return password;
	}
	
	@JsonbTransient 
	public void setPassword( String password) {
		this.password = HashTools.sha256HashCode(password);	
	}

	@JsonbProperty
	@XmlAttribute 
	public Address getAddress() {
		return address;
	}

	@JsonbTransient 
	public void setAddress(Address address) {
		this.address = address;
	}

	@JsonbProperty 
	@XmlAttribute 
	public Object getGroup() {
		return group;
	}
	
	@JsonbProperty 
	@XmlAttribute 
	public Set<Person> getPeopleObserving() {
		return peopleObserving;
	}

	@JsonbProperty 
	@XmlAttribute 
	public Set<Person> getPeopleObserved(){
		return peopleObserved;
	}
	
	@JsonbTransient 
	public void addPeopleObserving(Person person) {
		this.peopleObserving.add(person);
	}
	
	@JsonbProperty
	@XmlAttribute 
	public Set<Message> getMessages(){
		return messages;
	}
	
	@JsonbTransient 
	public void addMessage(Message message) {
		this.messages.add(message);
	}

}
