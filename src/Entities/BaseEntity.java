package Entities;

public class BaseEntity implements Comparable<Object>{
	
	private long id;
	private int version;
	private long creationTimestamp;

	
	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		return 0;
	}


	public long getCreationTimestamp() {
		return creationTimestamp;
	}


	public void setCreationTimestamp(long creationTimestamp) {
		this.creationTimestamp = creationTimestamp;
	}


	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}

}
