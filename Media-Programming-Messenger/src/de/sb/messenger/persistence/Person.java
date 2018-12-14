package de.sb.messenger.persistence;

import java.util.Collections;
import java.util.Set;

import javax.json.bind.annotation.JsonbProperty;
import javax.json.bind.annotation.JsonbTransient;
import javax.json.bind.annotation.JsonbVisibility;
import javax.persistence.CascadeType;
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
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import com.sun.xml.internal.txw2.annotation.XmlAttribute;

import de.sb.toolbox.bind.JsonProtectedPropertyStrategy;

@Entity
@Table(name="Person", schema="messenger")
@PrimaryKeyJoinColumn(name="personIdentity")
@JsonbVisibility(value = JsonProtectedPropertyStrategy.class)
@XmlRootElement
@XmlType
public class Person extends BaseEntity {
	

	static private final String DEFAULT_PASSWORD = "";
	static private final byte[] DEFAULT_PASSWORD_HASH = HashTools.sha256HashCode(DEFAULT_PASSWORD);
	
	
	@Embedded
	private Name name;
	
	@Embedded
	private Address address;
	
	@Email
	@Size(min = 3, max = 128)
	@Column(unique=true, nullable = false, updatable = true)
	@NotNull
	private String email;
	
	@Size(min = 32, max = 32)
	@Column(nullable = false, updatable = true)
	@NotNull
	private byte[] passwordHash;
	
	@OneToOne
	private Document avatar;
	
	@OneToMany(mappedBy = "author", cascade = {CascadeType.REMOVE, CascadeType.REFRESH})
	private Set<Message> messagesAuthored;
	
	@Enumerated
	private Group group;
	
	@ManyToMany(mappedBy = "peopleObserved", cascade = {CascadeType.REMOVE, CascadeType.REFRESH})
	private Set<Person> peopleObserving;

	@ManyToMany
	@JoinTable(
			schema = "messenger",
			name = "ObservationAssociation"
//			joinColumns = @JoinColumn(peopleObserved),
//			inverseJoinColumns = @JoinColumn(peopleObserving),
//			uniqueConstraints = 
	)
	private Set<Person> peopleObserved;
	
	
	
	protected Person() {
		this(null);
	}	
	
	public Person( Document avatar) {			
		this.name = new Name();
		this.email = null;
		this.passwordHash = DEFAULT_PASSWORD_HASH;
		this.avatar = avatar;
		this.messagesAuthored = Collections.emptySet();
		this.group = Group.USER;
		this.peopleObserving = Collections.emptySet();
		this.peopleObserved = Collections.emptySet();
		this.address = new Address();
	}
	
	
	@JsonbProperty
	@XmlAttribute
	protected long[] getPeopleObservingReference() {
		long[] observingIds = new long[peopleObserving.size()];
		int i =0;
		for(Person p: peopleObserving) {
			observingIds[i] = p.getIdentity();
			i++;
		}
		
		return observingIds;
	}
	
	@JsonbProperty
	@XmlAttribute
	protected long getAvatarReference(){
		return avatar.getIdentity();
	}
	
	@JsonbProperty
	@XmlAttribute
	protected long[] getPeopleObservedReferences(){
		long[] observedIds = new long[peopleObserved.size()];
		int i =0;
		for(Person p: peopleObserved) {
			observedIds[i] = p.getIdentity();
			i++;
		}
		return observedIds;
	}
	
	
	@JsonbProperty 
	@XmlAttribute 
	public Name getName() {
		return name;
	}
	
	@JsonbProperty
	@XmlAttribute 
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@JsonbTransient 
	@XmlTransient 
	public byte[] getPasswordHash() {
		return passwordHash;
	}
	
	public void setPasswordHash( byte[] passwordHash) {
		this.passwordHash = passwordHash;	
	}

	@JsonbProperty
	@XmlAttribute 
	public Address getAddress() {
		return address;
	}


	@JsonbProperty 
	@XmlAttribute 
	public Object getGroup() {
		return group;
	}
	
	@JsonbTransient 
	@XmlTransient
	public Set<Person> getPeopleObserving() {
		return peopleObserving;
	}

	@JsonbTransient 
	@XmlTransient	
	public Set<Person> getPeopleObserved(){
		return peopleObserved;
	}

	@JsonbTransient 
	@XmlTransient	
	public Set<Message> getMessagesAuthored(){
		return messagesAuthored;
	}
}
