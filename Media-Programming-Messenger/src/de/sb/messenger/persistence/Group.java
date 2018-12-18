package de.sb.messenger.persistence;

import javax.persistence.Embeddable;

@Embeddable
public enum Group {

	ADMIN, USER;
	
//	ADMIN("ADMIN"), USER("USER");
//
//    private final String text;
//
//
//    Group(final String text) {
//        this.text = text;
//    }
//
//    /* (non-Javadoc)
//     * @see java.lang.Enum#toString()
//     */
//    @Override
//    public String toString() {
//        return text;
//    }

}
