package de.sb.messenger.persistence;

public class Documents extends BaseEntity{
	
	private byte[] contentHash;
	private String contentType;
	private byte[] content;
	
	private Person owner;
	
	
	protected static void main(String[] arg) {
		Documents documents = new Documents();
	}
}
