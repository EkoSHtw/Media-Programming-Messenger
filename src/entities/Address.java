package entities;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class Address implements Comparable {
	
	@NotNull
	@Size(min = 1, max = 63)
	private String street;
	
	@NotNull
	@Size(min = 1, max = 15)
	private String postCode;
	
	@NotNull
	@Size(min = 1, max = 63)
	private String city;
	
	
	
	
	public Address(String street,String postCode, String city) {
		super();
		this.street = street;
		this.postCode = postCode;
		this.city = city;
	}

	
	
	
	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		return 0;
	}

	public String getStreet() {
		return street;
	}


	public void setStreet(String street) {
		this.street = street;
	}


	public String getPostCode() {
		return postCode;
	}


	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}


	public String getCity() {
		return city;
	}


	public void setCity(String city) {
		this.city = city;
	}

}
