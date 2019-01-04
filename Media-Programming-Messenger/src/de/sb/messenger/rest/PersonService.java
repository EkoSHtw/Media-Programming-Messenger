package de.sb.messenger.rest;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;
import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.OK;
import static de.sb.messenger.rest.BasicAuthenticationFilter.REQUESTER_IDENTITY;

import java.util.Comparator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.RollbackException;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import de.sb.messenger.persistence.Document;
import de.sb.messenger.persistence.Group;
import de.sb.messenger.persistence.Person;
import de.sb.toolbox.net.RestJpaLifecycleProvider;


@Path("/people")
public class PersonService {

	static private final String QUERY_PEOPLE = "Select p.identity from Person as p where "
			+ "(:surname is null or :surname = p.name.surname) and "
			+ "(:forename is null or :forename = p.name.forename) and "
			+ "(:email is null or :email = p.email)";
	
	static private final Comparator<Person> PERSON_COMPARATOR = Comparator
			.comparing(Person::getName)
			.thenComparing(Person::getEmail);
	
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
	public Person[] queryPeople (
			@QueryParam("surname") String surname,
			@QueryParam("forename") String forename, 
			@QueryParam("email") String email, 
			@QueryParam("street") String street,
			@QueryParam("postcode") String postcode,
			@QueryParam("city") String city,
			@QueryParam("group") Group group
	){
	
		final EntityManager em = RestJpaLifecycleProvider.entityManager("messenger");			
	
		int resultOffSet = 1;
		int resultLimit = 20;
		//so richtig????
		Person[] people = (Person[]) em.createQuery(QUERY_PEOPLE)
				.setParameter("surName", surname)
				.setParameter("foreName", forename).setParameter("email", email)
				.setFirstResult(resultOffSet).setMaxResults(resultLimit)
				.getResultList()
				.toArray();
		if (people == null) throw new ClientErrorException(NOT_FOUND);
		//if(people.length > 0) throw new ClientErrorException(OK);
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
	public long modifyPerson(
			@HeaderParam(REQUESTER_IDENTITY) @Positive final long requesterIdentity,
			@HeaderParam("Set-Password") @Size(min=4) String password,
			@NotNull Person personTemplate
	) {
		
		final EntityManager em = RestJpaLifecycleProvider.entityManager("messenger");
	
		final Person person;
		final boolean insertMode = personTemplate.getIdentity() == 0;
		if(insertMode) {
			//TODO create person
		}else{
			//TODO find person
		}
		//TODO get set data from person template into person
		
		if(insertMode) {
			//TODO em.persist(person);
		}else{
			//TODO em.flush();
		}
		
		try {
			em.getTransaction().commit();
		}catch(RollbackException e){
			throw new ClientErrorException(Status.CONFLICT);
		}finally {
			em.getTransaction().begin();
		}
		
		return person.getIdentity();
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
	public Person findPerson ( 
			@HeaderParam(REQUESTER_IDENTITY) @Positive final long requesterIdentity, 
			@PathParam("id") @PositiveOrZero final long personIdentity 
	) {
		final EntityManager em = RestJpaLifecycleProvider.entityManager("messenger");
		final long identity = personIdentity == 0 ? requesterIdentity : personIdentity;
		
		final Person person = em.find(Person.class, identity);
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
	public void createPerson(
			EntityManager em, 
			@FormParam("surName") String surName, 
			@FormParam("firstName") String firstName, 
			@FormParam("email") String email,
			@FormParam("street") String street, 
			@FormParam("postCode") String postCode,
			@FormParam("city") String city, 
			@FormParam("passwordHash") String passwordHash,
			@FormParam("avatar") Document avatar) {
		
		Person person = new Person(avatar);
		person.getAddress().setCity(city);
		person.getAddress().setPostCode(postCode);
		person.getAddress().setStreet(street);
		person.getName().setForename(firstName);
		person.getName().setSurname(surName);
		person.setEmail(email);
		//person.setPasswordHash(passwordHash);
		
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
	public void updatePerson(
			EntityManager em, 
			@FormParam("id") long identity, 
			@FormParam("surName")String surName,
			@FormParam("firstName") String firstName,
			@FormParam("email") String email, 
			@FormParam("street")String street, 
			@FormParam("postCode")String postCode, 
			@FormParam("city")String city, 
			@FormParam("avatar")Document avatar, 
			@FormParam("group")Group group) {
		
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
	public void updatePeopleObserved(
			EntityManager em, 
			@FormParam("id")long identity, 
			@FormParam("newObservedId") long newObservedId) {
		Person person = em.find(Person.class, identity);
		//TODO loop for newObser vedId if it is already in list remove else add
		//person.addPeopleObserving(em.find(Person.class, newObservedId));
	}
}
