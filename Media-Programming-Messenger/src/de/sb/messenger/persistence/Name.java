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
	private String surname;
	
	@NotNull
	@Size(min = 1, max = 31)
	@Column(nullable = false, updatable = true)
	private String forename;

	
	
	
	@Override
	public int compareTo(Name o) {
		int last = this.surname.compareTo(o.surname);
		return last == 0 ? this.forename.compareTo(o.forename) : last;
	}

	@JsonbProperty 
	@XmlAttribute
	public String getForename() {
		return forename;
	}
	
	public void setForename(String name) {
		this.forename = name;
	}
	
	@JsonbProperty 
	@XmlAttribute
	public String getSurname() {
		return surname;
	}
	
	public void setSurname(String name) {
		this.surname = name;
	}
	
	
}
