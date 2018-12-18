package de.sb.messenger.persistence;

import javax.json.bind.annotation.JsonbProperty;
import javax.json.bind.annotation.JsonbTransient;
import javax.json.bind.annotation.JsonbVisibility;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import com.sun.xml.internal.txw2.annotation.XmlAttribute;

import de.sb.toolbox.bind.JsonProtectedPropertyStrategy;


@Entity
@Table(name="Document", schema="messenger")
@PrimaryKeyJoinColumn(name="documentIdentity")
@JsonbVisibility(value = JsonProtectedPropertyStrategy.class)
@XmlRootElement
@XmlType
public class Document extends BaseEntity{
	
	static private final byte[] DEFAULT_CONTENT = new byte[0];
	static private final byte[] DEFAULT_CONTENT_HASH = HashTools.sha256HashCode(DEFAULT_CONTENT);
	
	@Column(nullable = false, updatable = false, insertable = true)
	@NotNull
	@Size(min = 32, max = 32)
	private byte[] contentHash;
	
	@Column(nullable = false, updatable = false, insertable = true)
	@NotNull
	@Size(min = 1, max = 63)
	private String contentType;

	@Column(nullable = false, updatable = false, insertable = true)
	@NotNull
	@Size(min = 1, max = 16777215)
	private static byte[] content;
		

	
	public Document() {
		super();
		this.content = DEFAULT_CONTENT;
		this.contentHash = DEFAULT_CONTENT_HASH;
		this.contentType = "application/octet-stream";
	}
	

	
	@JsonbProperty
	@XmlAttribute 
	public byte[] getContentHash() {
		return contentHash;
	}

	@JsonbProperty
	@XmlAttribute 
	public String getContentType() {
		return contentType;
	}

	@JsonbTransient
	@XmlTransient
	public byte[] getContent() {
		return content;
	}
	
	
	//TODO get image size and resize
	@JsonbProperty 
	@XmlAttribute 
	static public byte[] scaledImageContent(int width, int height) {
		return content;
	}
}
