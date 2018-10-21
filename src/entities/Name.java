package entities;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class Name implements Comparable {
	
	@NotNull
	@Size(min = 1, max = 31)
	private String surName;
	
	@NotNull
	@Size(min = 1, max = 31)
	private String firstName;

	
	

	protected static void main(String[] arg) {
		Name name = new Name("firstName", "surName");
	}
	
	public Name(String firstName, String surName) {
		super();
		this.firstName = firstName;
		this.surName = surName;
	}
	
	
	
	
	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		return 0;
	}

	public String getFirstName() {
		return firstName;
	}
	
	public String getSurName() {
		return surName;
	}

}
