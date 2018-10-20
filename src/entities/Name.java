package entities;

import javax.validation.constraints.Size;

public class Name implements Comparable {
	
	@Size(min = 1, max = 31)
	private String family;
	@Size(min = 1, max = 31)
	private String given;
	@Size(min = 1, max = 31)
	private String firstName;

	

	protected static void main(String[] arg) {
		Name name = new Name("family", "given");
	}
	
	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		return 0;
	}

	public Name(@Size(min = 1, max = 31) String family, @Size(min = 1, max = 31) String given) {
		super();
		this.family = family;
		this.given = given;
	}

	public String getFamily() {
		return family;
	}

	
	public String getGiven() {
		return given;
	}


	public String getFirstName() {
		return firstName;
	}



}
