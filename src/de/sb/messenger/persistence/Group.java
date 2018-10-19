package de.sb.messenger.persistence;

import java.util.List;

public class Group implements Comparable{
	
	public Person ADMIN;
	public List<Person> USER;

	protected static void main(String[] arg) {
		Group group = new Group();
	}
	
	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		return 0;
	}

}
