package de.sb.messenger.persistence;

import javax.validation.constraints.Size;

public class Documents extends BaseEntity{
	
	@Size(min = 32, max = 32)
	private byte contentHash;
	
	@Size(min = 1, max = 63)
	private String contentType;
	
	@Size(min = 1, max = 16777215)
	private byte content;
	
	public Documents(long identity, int version, long creationTimestamp, @Size(min = 32, max = 32) byte contentHash,
			@Size(min = 1, max = 63) String contentType, @Size(min = 1, max = 16777215) byte content, String fileType,
			Person owner) {
		
		super(identity, version, creationTimestamp);
		this.contentHash = contentHash;
		this.contentType = contentType;
		this.content = content;
		this.fileType = fileType;
		this.owner = owner;
	}

	private String fileType;
	
	private Person owner;
	
	
	protected static void main(String[] arg) {
		
		//Documents documents = new Documents(1, 1, System.currentTimeMillis(), null, "xml", null, "html", new Person());
	}
	
	public byte _scaledImageContent(int width, int height) {
		
		return content;
		//return fileType * content * width * height;
	}
}
