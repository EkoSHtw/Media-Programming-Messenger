package de.sb.messenger.rest;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;
import static javax.ws.rs.core.Response.Status.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.constraints.Positive;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import de.sb.messenger.persistence.Address;
import de.sb.messenger.persistence.BaseEntity;
import de.sb.messenger.persistence.Document;
import de.sb.messenger.persistence.Group;
import de.sb.messenger.persistence.HashTools;
import de.sb.messenger.persistence.Message;
import de.sb.messenger.persistence.Name;
import de.sb.messenger.persistence.Person;
import de.sb.toolbox.net.RestJpaLifecycleProvider;

@Path("/people")
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
	public Person[] getPeople (@HeaderParam("surName") String surName,@HeaderParam("firstName") String firstName, @HeaderParam("email")String email,@HeaderParam("street") String street, @HeaderParam("postCode")String postCode, @HeaderParam("city")String city, @HeaderParam("group")Group group) {
		Person[] people = null;
	
		final EntityManager em = RestJpaLifecycleProvider.entityManager("messenger");	
		//people = em.find(surNameemail, firstName, email,  street, city);
		Query query = em.createQuery("Select " + surName + " from Person");
		List<Person> pList = query.getResultList();
		
		
		System.out.println("get people");
		if (people == null) throw new ClientErrorException(NOT_FOUND);
		
		if (!em.isOpen()) throw new ClientErrorException(INTERNAL_SERVER_ERROR); 

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
	@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Produces(MediaType.TEXT_PLAIN)
	public void setPeople(@HeaderParam("identity") long identity,@HeaderParam("surName") String surName,@HeaderParam("firstName") String firstName,@HeaderParam("email") String email, @HeaderParam("street")String street, @HeaderParam("postCode")String postCode, 
			@HeaderParam("city") String city, @HeaderParam("password") String password,@HeaderParam("avatar") Document avatar, @HeaderParam("group")Group group) {
		
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
	@Path("/{id}")
	@Produces({ APPLICATION_JSON, APPLICATION_XML })
	public Person getPerson ( 
			//@HeaderParam(REQUESTER_IDENTITY) @Positive final long Requester_identity, 
			@PathParam("id") @Positive final long personIdentity ) {
		
		final EntityManager em = RestJpaLifecycleProvider.entityManager("messenger");
		final Person person = em.find(Person.class, personIdentity);
		if (person == null) throw new ClientErrorException(NOT_FOUND);

		return person;
	}
	
	
	
	
	
	
	/**
	 * Returns the avatar content of the person matching the given identity and it's content type
	 * @param personIdentity the person identity
	 * @return the document for the person (HTTP 200)
	 * @throws ClientErrorException (HTTP 404) if the given avatar cannot be found
	 * @throws PersistenceException (HTTP 500) if there is a problem with the persistence layer
	 * @throws IllegalStateException (HTTP 500) if the entity manager associated with the current thread is not open
	 */
	@GET
	@Path("/{id}/avatar")
	@Produces({ APPLICATION_JSON, APPLICATION_XML })
	public Document getAvatar (@PathParam("id") @Positive final long personIdentity) {
		
		final EntityManager em = RestJpaLifecycleProvider.entityManager("messenger");
		//TODO does it already work? need to add mediatype
		final Document avatar = em.find(Document.class, personIdentity);
		
		if (avatar == null) throw new ClientErrorException(NOT_FOUND);
		
		return avatar;
	}
	
	
	
	
	
	/**
	 * Updates the avatar content of the person matching the given identity
	 * @param personIdentity the person identity
	 * @return the document for the person (HTTP 200)
	 * @throws ClientErrorException (HTTP 404) if the given person cannot be found
	 * @throws PersistenceException (HTTP 500) if there is a problem with the persistence layer
	 * @throws IllegalStateException (HTTP 500) if the entity manager associated with the current thread is not open
	 */
	@PUT
	@Consumes({ APPLICATION_JSON, APPLICATION_XML })
	@Produces({ APPLICATION_JSON, APPLICATION_XML })
	@Path("/{id}/avatar")
	public void updateAvatar (@PathParam("id") @Positive final long personIdentity) {
		
		final EntityManager em = RestJpaLifecycleProvider.entityManager("messenger");
		//TODO hier muss die person auch �ber das dokument geholt werden ? -> seltsam... besser f�nde ich das doku �ber die person
		final Person person = em.find(Person.class, personIdentity);
		if (person == null) throw new ClientErrorException(NOT_FOUND);
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
	@POST
	@Consumes({ APPLICATION_JSON, APPLICATION_XML })
	@Produces({ APPLICATION_JSON, APPLICATION_XML })
	private void createPerson(EntityManager em, String surName, String firstName, String email,String street, 
			String postCode,String city, String password,Document avatar) {
		
		Person person = new Person(avatar);
		Address address = new Address(street, postCode, city);
		Name name = new Name(firstName, surName);
		person.setAddress(address);
		person.setName(name);
		person.setEmail(email);
		person.setPassword(password);
		
		em.getTransaction().begin();
		em.persist(person);
		em.getTransaction().commit();
		em.flush();
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
	@PUT
	@Consumes({ APPLICATION_JSON, APPLICATION_XML })
	@Produces({ APPLICATION_JSON, APPLICATION_XML })
	@Path("/{id}")
	private void updatePerson(EntityManager em, @PathParam("id") long identity, @PathParam("surName")String surName,@PathParam("firstName") String firstName,@PathParam("email") String email, 
			@PathParam("street")String street, @HeaderParam("postCode")String postCode, @PathParam("city")String city, @PathParam("avatar")Document avatar, Group group) {
		
		Person person = em.find(Person.class, identity);

		em.getTransaction().begin();
		person.setEmail(email);
		//TODO implement here update function
		em.getTransaction().commit();
		em.flush();
	}
	
	@PUT
	@Consumes({ APPLICATION_JSON, APPLICATION_XML })
	@Produces({ APPLICATION_JSON, APPLICATION_XML })
	@Path("/{id}/peopleObserved")
	private void updatePeopleObserved(EntityManager em, @PathParam("id")long identity, long newObservedId) {
		Person person = em.find(Person.class, identity);
		//TODO 
		//loop for newObser vedId if it is already in list remove else add
		person.addPeopleObserving(em.find(Person.class, newObservedId));
	}
}
