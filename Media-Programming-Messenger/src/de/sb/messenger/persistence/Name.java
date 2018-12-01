package de.sb.messenger.persistence;

import javax.json.bind.annotation.JsonbProperty;
import javax.json.bind.annotation.JsonbTransient;
import javax.json.bind.annotation.JsonbVisibility;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlAccessType;
import de.sb.toolbox.bind.JsonProtectedPropertyStrategy;

@Embeddable
@JsonbVisibility(value = JsonProtectedPropertyStrategy.class)
@XmlAccessorType(XmlAccessType.NONE)
public class Name implements Comparable<Name> {
	
//	@NotNull
	@Size(min = 1, max = 31)
	private String surName;
	
//	@NotNull
	@Size(min = 1, max = 31)
	private String firstName;

	
	

	protected Name() {
		this(null, null);
	}
	
	
	public Name(String firstName, String surName) {
		super();
		this.firstName = firstName;
		this.surName = surName;
	}
	
	
	@Override
	public int compareTo(Name o) {
		int last = this.surName.compareTo(o.surName);
		return last == 0 ? this.firstName.compareTo(o.firstName) : last;
	}

	@JsonbProperty 
	@XmlAttribute
	public String getFirstName() {
		return firstName;
	}
	
	@JsonbProperty 
	@XmlAttribute
	public String getSurName() {
		return surName;
	}
	
	@JsonbTransient
	@XmlTransient
	public void setSurname(String name) {
		this.surName = name;
	}
	
	@JsonbTransient
	@XmlTransient
	public void setFirstName(String name) {
		this.firstName = name;
	}
}