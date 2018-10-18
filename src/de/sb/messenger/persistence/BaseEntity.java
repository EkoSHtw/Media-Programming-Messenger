package de.sb.messenger.persistence;

public class BaseEntity implements Comparable<BaseEntity>{
	
	private long identity;
	private int version;
	private long creationTimestamp;




	public long getCreationTimestamp() {
		return creationTimestamp;
	}


	public void setCreationTimestamp(long creationTimestamp) {
		this.creationTimestamp = creationTimestamp;
	}


	public long getIdentity() {
		return identity;
	}


	public void setIdentity(long id) {
		this.identity = id;
	}


	@Override
	public int compareTo(BaseEntity o) {
		// TODO Auto-generated method stub
		if(this.identity < o.getIdentity())
		return 0;
		else return 1;
	}


	public Object getMessagesCaused() {
		// TODO Auto-generated method stub
		return null;
	}

}
