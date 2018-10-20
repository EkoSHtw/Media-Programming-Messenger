package entities;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class BaseEntity implements Comparable<BaseEntity>{
	
	@NotEmpty
	@NotNull
	protected long identity;
	

	@NotEmpty
	@NotNull
	protected int version;
	

	@NotEmpty
	@NotNull
	protected long creationTimestamp;

	
	protected static void main(String[] arg) {
		BaseEntity baseEntity = new BaseEntity(12345, 1, System.currentTimeMillis());
	}


	public BaseEntity(long identity, int version, long creationTimestamp) {
		super();
		this.identity = identity;
		this.version = version;
		this.creationTimestamp = creationTimestamp;
	}


	@Override
	public String toString() {
		return  this.getClass().getSimpleName() +"@" + identity;
	}


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
