package entities;

import java.util.ArrayList;
import java.util.List;

import javax.json.bind.annotation.JsonbProperty;
import javax.json.bind.annotation.JsonbTransient;
import javax.json.bind.annotation.JsonbVisibility;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
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
	private String email;
	
	@NotNull
	@Size(min = 32, max = 32)
	private byte[] password;
	
	@NotNull
	@OneToOne(mappedBy = "Person")
	@JoinColumn(name = "IDENTITY_ID")
	private Document avatar;
	
	@NotNull
	@OneToMany(mappedBy = "Person")
	@JoinColumn(name = "IDENTITY_ID")
	private List<Message> messages;
	
	@NotNull
	@Enumerated
	private Group group;
	
	@NotNull
	@ManyToMany
	@JoinColumn(name = "IDENTITY_ID")
	private List<Person> peopleObserving;

	@NotNull
	@ManyToMany
	@JoinColumn(name = "IDENTITY_ID")
	private List<Person> peopleObserved;
	
	protected Person() {}
	
	public Person(Name name, String email, String password, Document avatar, Group group, Address address) {
		
		super();
		
		this.name = name;
		this.email = email;
		this.password = HashTools.sha256HashCode(password);
		this.avatar = avatar;
		this.messages = new ArrayList<Message>();
		this.group = group;
		this.peopleObserving = new ArrayList<Person>();
		this.address = address;
	}
	
	
	
	@JsonbProperty 
	protected long[] getPeopleObservingReference() {
		long[] observingIds = new long[peopleObserving.size()];
		for (int i=0; i< peopleObserving.size(); i++) {
			observingIds[i] = peopleObserving.get(i).getIdentity();
		}
		
		return observingIds;
	}
	
	@JsonbProperty 
	protected long getAvatarReference(){
		return avatar.getIdentity();
	}
	
	@JsonbProperty 
	protected long[] getPeopleObservedReferences(){
		long[] observedIds = new long[peopleObserved.size()];
		for (int i =0; i < peopleObserved.size(); i++) {
			observedIds[i] = peopleObserved.get(i).identity;
		}
		return observedIds;
	}
	
	

	
	@JsonbProperty 
	public Name getName() {
		return name;
	}

	@JsonbProperty 
	public String getEmail() {
		return email;
	}

	@JsonbTransient 
	public void setEmail(String email) {
		this.email = email;
	}

	@JsonbProperty 
	public byte[] getPasswordHash() {
		return password;
	}
	
	@JsonbTransient 
	public void setPassword( String password) {
		this.password = HashTools.sha256HashCode(password);	
	}

	@JsonbProperty 
	public Address getAddress() {
		return address;
	}

	@JsonbTransient 
	public void setAddress(Address address) {
		this.address = address;
	}

	@JsonbProperty 
	public Object getGroup() {
		return group;
	}
	
	@JsonbProperty 
	public List<Person> getPeopleObserving() {
		return peopleObserving;
	}

	@JsonbProperty 
	public List<Person> getPeopleObserved(){
		return peopleObserved;
	}
	
	@JsonbTransient 
	public void addPeopleObserving(Person person) {
		this.peopleObserving.add(person);
	}
	
	@JsonbProperty 
	public List<Message> getMessages(){
		return messages;
	}
	
	@JsonbTransient 
	public void addMessage(Message message) {
		this.messages.add(message);
	}

}
