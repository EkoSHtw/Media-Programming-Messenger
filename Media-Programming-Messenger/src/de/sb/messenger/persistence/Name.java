package de.sb.messenger.persistence;

import javax.json.bind.annotation.JsonbProperty;
import javax.json.bind.annotation.JsonbVisibility;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlAccessType;
import de.sb.toolbox.bind.JsonProtectedPropertyStrategy;

@Embeddable
@JsonbVisibility(value = JsonProtectedPropertyStrategy.class)
@XmlAccessorType(XmlAccessType.NONE)
public class Name implements Comparable<Name> {
	
	@NotNull
	@Size(min = 1, max = 31)
	@Column(nullable = false, updatable = true)
	private String surName;
	
	@NotNull
	@Size(min = 1, max = 31)
	@Column(nullable = false, updatable = true)
	private String foreName;

	
	
	
	@Override
	public int compareTo(Name o) {
		int last = this.surName.compareTo(o.surName);
		return last == 0 ? this.foreName.compareTo(o.foreName) : last;
	}

	@JsonbProperty 
	@XmlAttribute
	public String getForename() {
		return foreName;
	}
	
	public void setForename(String name) {
		this.foreName = name;
	}
	
	@JsonbProperty 
	@XmlAttribute
	public String getSurName() {
		return surName;
	}
	
	public void setSurname(String name) {
		this.surName = name;
	}
	
	
}
