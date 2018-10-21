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
	
	private List<Documents> documents;
	
	private List<Message> messages;
	
	private Group group;
	
	private List<Person> peopleObserving;
	
	public enum Group{
		_ADMIN,
		_USER;
	}

	
	
	
	protected static void main(String[] arg) {
		//Person person = new Person();
	}
	
	public Person(long identity, int version, long creationTimestamp, Name name, @Size(min = 0, max = 128) String email,
			@Size(min = 32, max = 32) String password, List<Documents> documents, List<Message> messages, Group group,
			List<Person> peopleObserving, Address address) {
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
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		return null;
	}

	public List<Person> getPeopleObserving() {
		return peopleObserving;
	}

	public void addPeopleObserving(Person person) {
		this.peopleObserving.add(person);
	}

}
