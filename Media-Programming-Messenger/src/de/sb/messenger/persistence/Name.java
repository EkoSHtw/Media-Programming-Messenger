package de.sb.messenger.persistence;

import javax.json.bind.annotation.JsonbProperty;
import javax.json.bind.annotation.JsonbVisibility;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import de.sb.toolbox.bind.JsonProtectedPropertyStrategy;

@Embeddable
@JsonbVisibility(value = JsonProtectedPropertyStrategy.class)
@XmlAccessorType(XmlAccessType.NONE)
public class Name implements Comparable<Name> {
	
	@NotNull
	@Size(min = 1, max = 31)
	@Column(name = "surname", nullable = false, updatable = true)
	private String family;
	
	@NotNull
	@Size(min = 1, max = 31)
	@Column(name = "forename", nullable = false, updatable = true)
	private String given;

	
	
	
	@Override
	public int compareTo(Name o) {
		int last = this.family.compareTo(o.family);
		return last == 0 ? this.given.compareTo(o.given) : last;
	}

	@JsonbProperty 
	@XmlAttribute
	public String getGiven() {
		return given;
	}
	
	public void setGiven(String name) {
		this.given = name;
	}
	
	@JsonbProperty 
	@XmlAttribute
	public String getFamily() {
		return family;
	}
	
	public void setFamily(String name) {
		this.family = name;
	}
	
	
}
