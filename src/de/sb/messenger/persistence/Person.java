package de.sb.messenger.persistence;

public class Person extends BaseEntity {
	
	private Name name;
	private String email;
	private byte[] password;
	
	private Address address;


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

	public  byte[] getPasword() {
		return password;
	}

	public void setPasword( byte[] password) {
		this.password = password;
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

	public byte[] getPasswordHash() {
		// TODO Auto-generated method stub
		return password;
	}

}
