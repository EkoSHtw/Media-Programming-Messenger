package entities;

import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class Person extends BaseEntity {
	
	@NotNull
	private Name name;
	
	@NotNull
	private Address address;
	
	@NotNull
	@Email
	@Size(min = 3, max = 128)
	private String email;
	
	@NotNull
	@Size(min = 32, max = 32)
	private byte[] password;
	
	@NotNull
	private List<Document> documents;
	
	@NotNull
	private List<Message> messages;
	
	@NotNull
	private Group group;
	
	@NotNull
	private List<Person> peopleObserving;

	
	
	
	protected Person() {
		//Person person = new Person();
	}
	
	public Person(long identity, int version, long creationTimestamp, Name name, String email, String password, 
			List<Document> documents, List<Message> messages, Group group,List<Person> peopleObserving, Address address) {
		
		super(identity, version, creationTimestamp);
		
		this.name = name;
		this.email = email;
		this.password = HashTools.sha256HashCode(password);
		this.documents = documents;
		this.messages = messages;
		this.group = group;
		this.peopleObserving = peopleObserving;
		this.address = address;
	}
	

	
	
	public Name getName() {
		return name;
	}

	public void setName(Name name) {
		this.name = name;
	}

	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	
	public byte[] getPasswordHash() {
		return password;
	}
	
	public void setPassword( String password) {
		this.password = HashTools.sha256HashCode(password);	
	}

	
	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	
	public Object getGroup() {
		return group;
	}

	public List<Person> getPeopleObserving() {
		return peopleObserving;
	}

	public void addPeopleObserving(Person person) {
		this.peopleObserving.add(person);
	}

}
