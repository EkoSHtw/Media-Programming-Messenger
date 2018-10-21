package entities;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Entity
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
	@PrimaryKeyJoinColumn
	private Person owner;
	

	
	
	protected Document() {
		//Documents documents = new Documents(1, 1, System.currentTimeMillis(), null, "xml", null, "html", new Person());
	}
	
	public Document(long identity, int version, long creationTimestamp, byte contentHash,
			String contentType, String content, String fileType, Person owner) {
		
		super(identity, version, creationTimestamp);
		this.contentHash = contentHash;
		this.contentType = contentType;
		this.content = HashTools.sha256HashCode(content);
		this.fileType = fileType;
		this.owner = owner;
	}
	

	
	
	public byte getContentHash() {
		return contentHash;
	}

	public String getContentType() {
		return contentType;
	}

	public byte[] getContent() {
		return content;
	}

	public String getFileType() {
		return fileType;
	}

	public Person getOwner() {
		return owner;
	}
	
	public byte[] _scaledImageContent(int width, int height) {
		return content;
		//return fileType * content * width * height;
	}
}
