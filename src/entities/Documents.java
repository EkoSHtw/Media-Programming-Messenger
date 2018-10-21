package entities;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class Documents extends BaseEntity{
	
	@NotEmpty
	@NotNull
	@Size(min = 32, max = 32)
	private byte contentHash;
	
	@NotNull
	@NotEmpty
	@Size(min = 1, max = 63)
	private String contentType;

	@NotNull
	@NotEmpty
	private String fileType;
	
	@Size(min = 1, max = 16777215)
	private byte[] content;
	
	@NotNull
	@NotEmpty
	private Person owner;
	

	
	
	protected static void main(String[] arg) {
		//Documents documents = new Documents(1, 1, System.currentTimeMillis(), null, "xml", null, "html", new Person());
	}
	
	public Documents(long identity, int version, long creationTimestamp, @Size(min = 32, max = 32) byte contentHash,
			@Size(min = 1, max = 63) String contentType, @Size(min = 1, max = 16777215) String content, String fileType,
			Person owner) {
		
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
