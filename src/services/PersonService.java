package services;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.validation.constraints.Positive;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import de.sb.toolbox.net.RestJpaLifecycleProvider;
import entities.Address;
import entities.Document;
import entities.Group;
import entities.HashTools;
import entities.Message;
import entities.Name;
import entities.Person;

@Path("people")
public class PersonService {

	
	/**
	 * Returning all people matching the given criteria.
	 * @param surName
	 * @param firstName
	 * @param email
	 * @param street
	 * @param postCode
	 * @param city
	 * @param group
	 * @return the matching people (HTTP 200)
	 * @throws ClientErrorException (HTTP 404) no people are found
	 * @throws PersistenceException (HTTP 500) if there is a problem with the persistence layer
	 * @throws IllegalStateException (HTTP 500) if the entity manager associated with the current thread is not open
	 */
	@GET
	@Produces({ APPLICATION_JSON, APPLICATION_XML })
	public Person[] getPeople ( String surName, String firstName, String email, String street, String postCode, String city, Group group) {
		final Person[] people = null;
		
		final EntityManager em = RestJpaLifecycleProvider.entityManager("messenger");
		
		//people = em.find(); TODO implement here search function
		if (people == null) throw new ClientErrorException(NOT_FOUND);

		return people;
	}
	
	/**
	 * Creates or updates a person
	 * @param identity
	 * @param surName
	 * @param firstName
	 * @param email
	 * @param street
	 * @param postCode
	 * @param city
	 * @param password
	 * @param avatar
	 * @param group
	 * @return the matching people (HTTP 200)
	 * @throws ClientErrorException (HTTP 404) person is not found
	 * @throws PersistenceException (HTTP 500) if there is a problem with the persistence layer
	 * @throws IllegalStateException (HTTP 500) if the entity manager associated with the current thread is not open
	 */
	@POST
	@Produces({ APPLICATION_JSON, APPLICATION_XML })
	public void setPeople( long identity, String surName, String firstName, String email, String street, String postCode, 
			String city, String password, Document avatar, Group group) {
		
		final EntityManager em = RestJpaLifecycleProvider.entityManager("messenger");
		
		if(identity == 0) {
			createPerson(em, surName, firstName, email, street, postCode, city, password, avatar);
		}else{
			updatePerson(em, identity, surName, firstName, email, street, postCode, city, avatar, group);
		}
	}
	
	/**
	 * Returns the person with the given identity.
	 * @param personIdentity the person identity
	 * @return the matching person (HTTP 200)
	 * @throws ClientErrorException (HTTP 404) if the given person cannot be found
	 * @throws PersistenceException (HTTP 500) if there is a problem with the persistence layer
	 * @throws IllegalStateException (HTTP 500) if the entity manager associated with the current thread is not open
	 */
	@GET
	@Path("{id}")
	@Produces({ APPLICATION_JSON, APPLICATION_XML })
	public Person getPerson ( @PathParam("id") @Positive final long personIdentity ) {
		
		final EntityManager em = RestJpaLifecycleProvider.entityManager("messenger");
		final Person person = em.find(Person.class, personIdentity);
		if (person == null) throw new ClientErrorException(NOT_FOUND);

		return person;
	}
	
	/**
	 * Creates a new person
	 * @param em
	 * @param surName
	 * @param firstName
	 * @param email
	 * @param street
	 * @param postCode
	 * @param city
	 * @param password
	 * @param avatar
	 */
	private void createPerson(EntityManager em, String surName, String firstName, String email, String street, 
			String postCode, String city, String password, Document avatar) {
		
		Address address = new Address(street, postCode, city);
		Name name = new Name(firstName, surName);
		Group group = Group._USER;
		Person person = new Person(name, email, password, avatar, group, address);
		
		em.getTransaction().begin();
		em.persist(person);
		em.getTransaction().commit();
	}
	
	/**
	 * Updates an existing person
	 * @param em
	 * @param identity
	 * @param surName
	 * @param firstName
	 * @param email
	 * @param street
	 * @param postCode
	 * @param city
	 * @param avatar
	 * @param group
	 */
	private void updatePerson(EntityManager em, long identity, String surName, String firstName, String email, 
			String street, String postCode, String city, Document avatar, Group group) {
		
		Person person = em.find(Person.class, identity);

		em.getTransaction().begin();
		person.setEmail(email);
		//TODO implement here update function
		em.getTransaction().commit();
	}
}
