package de.sb.messenger.persistence;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.json.bind.annotation.JsonbProperty;
import javax.json.bind.annotation.JsonbTransient;
import javax.json.bind.annotation.JsonbVisibility;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
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
	@Valid
	private Name name;
	
	@Embedded
	@Valid
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
	@JoinColumn(name="avatarReference", nullable = false, updatable = true)
	private Document avatar;
	

	@OneToMany(mappedBy = "author", cascade = {CascadeType.REMOVE, CascadeType.REFRESH})
	private Set<Message> messagesAuthored;
	
	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "groupAlias", nullable = false, updatable = true)
	private Group group;
	
	@NotNull
	@ManyToMany(mappedBy = "peopleObserved", cascade = {CascadeType.REMOVE, CascadeType.REFRESH})
	private Set<Person> peopleObserving;

	@NotNull
	@ManyToMany
	@JoinTable(
			schema = "messenger",
			name = "ObservationAssociation",
			joinColumns = @JoinColumn(name = "observingReference"),
			inverseJoinColumns = @JoinColumn(name = "observedReference"),
			uniqueConstraints = @UniqueConstraint(columnNames = {"observingReference", "observedReference"})
	)
	private Set<Person> peopleObserved;
	
	
	
	protected Person() {
		this(null);
	}	
	
	public Person( Document avatar) {			
		this.name = new Name();
		this.address = new Address();
		this.email = null;
		this.passwordHash = DEFAULT_PASSWORD_HASH;
		this.avatar = avatar;
		this.messagesAuthored = Collections.emptySet();
		this.group = Group.USER;
		this.peopleObserving = Collections.emptySet();
		this.peopleObserved = new HashSet<>();
	}
	
	
	@JsonbProperty
	@XmlElement
	protected long[] getPeopleObservingReferences() {
		return this.peopleObserving.stream().sorted().mapToLong(Person::getIdentity).toArray();
	}
	
	@JsonbProperty
	@XmlAttribute
	protected long getAvatarReference(){
		return this.avatar == null ? 0 : this.avatar.getIdentity();
	}
	
	@JsonbProperty
	@XmlElement
	protected long[] getPeopleObservedReferences(){
		return this.peopleObserved.stream().sorted().mapToLong(Person::getIdentity).toArray();
	}
	
	
	@JsonbProperty 
	@XmlElement
	public Name getName() {
		return name;
	}
	
	protected void setName (Name name) {
		this.name = name;
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
	@XmlElement
	public Address getAddress() {
		return address;
	}
	
	protected void setAddress(Address address) {
		this.address = address;
	}
	
	@JsonbTransient
	@XmlTransient
	public Document getAvatar() {
		return avatar;
	}
	
	public void setAvatar(Document avatar) {
		this.avatar = avatar;
	}


	@JsonbProperty 
	@XmlElement
	public Group getGroup() {
		return group;
	}
	
	public void setGroup(Group group) {
		this.group = group;
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
