package entities;

import javax.json.bind.annotation.JsonbVisibility;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import de.sb.toolbox.bind.JsonProtectedPropertyStrategy;

@Embeddable
@JsonbVisibility(value = JsonProtectedPropertyStrategy.class)
@XmlAccessorType(XmlAccessType.NONE)
public class Address implements Comparable<Address> {
	
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




	@Override
	public int compareTo(Address o) {
		int last = this.city.compareTo(o.city);
		return last;
	}

}