package entities;

import javax.json.bind.annotation.JsonbProperty;
import javax.json.bind.annotation.JsonbVisibility;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import de.sb.toolbox.bind.JsonProtectedPropertyStrategy;


@Entity
@Table(name="Documents", schema="messenger")
@PrimaryKeyJoinColumn(name="IDENTITY_ID")
@JsonbVisibility(value = JsonProtectedPropertyStrategy.class)
@XmlRootElement
@XmlType
public class Document extends BaseEntity{
	
	@NotEmpty
	@Size(min = 32, max = 32)
	private byte contentHash;

	@NotEmpty
	@Size(min = 1, max = 63)
	private String contentType;

	@NotEmpty
	private String fileType;
	
	@NotEmpty
	@Size(min = 1, max = 16777215)
	private byte[] content;
	
	@NotNull
	@OneToOne
	@JoinColumn(name = "IDENTITY_ID")
	private Person owner;
	

	
	
	protected Document() {
		//Documents documents = new Documents(1, 1, System.currentTimeMillis(), null, "xml", null, "html", new Person());
	}
	
	public Document( byte contentHash, String contentType, String content, String fileType, Person owner) {
		
		super();
		this.contentHash = contentHash;
		this.contentType = contentType;
		this.content = HashTools.sha256HashCode(content);
		this.fileType = fileType;
		this.owner = owner;
	}
	

	
	@JsonbProperty 
	public byte getContentHash() {
		return contentHash;
	}

	@JsonbProperty 
	public String getContentType() {
		return contentType;
	}

	@JsonbProperty 
	public byte[] getContent() {
		return content;
	}

	@JsonbProperty 
	public String getFileType() {
		return fileType;
	}

	@JsonbProperty 
	public Person getOwner() {
		return owner;
	}
	
	@JsonbProperty 
	public byte[] _scaledImageContent(int width, int height) {
		return content;
		//return fileType * content * width * height;
	}
}
