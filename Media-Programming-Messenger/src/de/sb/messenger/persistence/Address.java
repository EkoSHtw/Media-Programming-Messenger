package de.sb.messenger.persistence;

import javax.json.bind.annotation.JsonbProperty;
import javax.json.bind.annotation.JsonbVisibility;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.persistence.Column;
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
	@Column(nullable = false, updatable = true)
	private String street;
	
	@NotNull
	@Size(min = 1, max = 15)
	@Column(nullable = false, updatable = true)
	private String postCode;

	@NotNull
	@Size(min = 1, max = 63)
	@Column(nullable = false, updatable = true)
	private String city;
	
	
	
	@JsonbProperty 
	@XmlAttribute
	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	@JsonbProperty
	@XmlAttribute
	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	@JsonbProperty
	@XmlAttribute
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
