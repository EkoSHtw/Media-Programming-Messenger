package de.sb.messenger.persistence;

import javax.json.bind.annotation.JsonbProperty;
import javax.json.bind.annotation.JsonbTransient;
import javax.json.bind.annotation.JsonbVisibility;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import de.sb.toolbox.bind.JsonProtectedPropertyStrategy;

@Embeddable
@JsonbVisibility(value = JsonProtectedPropertyStrategy.class)
@XmlAccessorType(XmlAccessType.NONE)
public class Address implements Comparable<Address> {
	
	@Size(min = 1, max = 63)
	private String street;
	
	@Size(min = 1, max = 15)
	private String postCode;

	@Size(min = 1, max = 63)
	private String city;
	
	
	protected Address() {
		this(null, null, null);
	}
	
	public Address(String street,String postCode, String city) {
		super();
		this.street = street;
		this.postCode = postCode;
		this.city = city;
	}

	
	
	
	@JsonbProperty 
	@XmlAttribute
	public String getStreet() {
		return street;
	}

	@JsonbTransient
	@XmlTransient
	public void setStreet(String street) {
		this.street = street;
	}

	@JsonbProperty
	@XmlAttribute
	public String getPostCode() {
		return postCode;
	}

	@JsonbTransient
	@XmlTransient
	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	@JsonbProperty
	@XmlAttribute
	public String getCity() {
		return city;
	}

	@JsonbTransient
	@XmlTransient
	public void setCity(String city) {
		this.city = city;
	}




	@Override
	public int compareTo(Address o) {
		int last = this.city.compareTo(o.city);
		return last;
	}

}
