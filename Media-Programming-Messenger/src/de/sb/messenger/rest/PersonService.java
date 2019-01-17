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

import com.sun.istack.internal.Nullable;

import de.sb.messenger.persistence.Document;
import de.sb.messenger.persistence.Group;
import de.sb.messenger.persistence.HashTools;
import de.sb.messenger.persistence.Person;
import de.sb.toolbox.net.RestJpaLifecycleProvider;


@Path("/people")
public class PersonService {

	static private final String QUERY_PEOPLE = "Select p.identity from Person as p where "
			+ "(:surname is null or :surname = p.name.surname) and "
			+ "(:forename is null or :forename = p.name.forename) and "
			+ "(:email is null or :email = p.email) and "
			+ "(:street is null or :street = p.address.street) and "
			+ "(:postCode is null or :postCode = p.address.postCode) and "
			+ "(:city is null or :city = p.address.city) and "
			+ "(:groupAlias is null or :groupAlias = p.groupAlias)";
	
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
	 * @param groupAlias
	 * @return the matching people (HTTP 200)
	 * @throws ClientErrorException (HTTP 404) no people are found
	 * @throws PersistenceException (HTTP 500) if there is a problem with the persistence layer
	 * @throws IllegalStateException (HTTP 500) if the entity manager associated with the current thread is not open
	 */
	@GET
	@Produces({ APPLICATION_JSON, APPLICATION_XML })
	public Person[] queryPeople (
			@QueryParam("email") String email,
			@QueryParam("surname") String surname,
			@QueryParam("forename") String forename, 
			@QueryParam("street") String street,
			@QueryParam("city") String city
	){
	
		final EntityManager em = RestJpaLifecycleProvider.entityManager("messenger");			
	
		int resultOffSet = 1;
		int resultLimit = 20;
		Person[] people = (Person[]) em.createQuery(QUERY_PEOPLE) // TODO ! cant convert object to person
				.setParameter("surname", surname)
				.setParameter("forename", forename)
				.setParameter("email", email)
				.setParameter("street", street)
				.setParameter("city", city)
				.setFirstResult(resultOffSet)
				.setMaxResults(resultLimit)
				.getResultList()
				.toArray();
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
	public long modifyPerson(
			@HeaderParam(REQUESTER_IDENTITY) @Positive final long requesterIdentity,
			@HeaderParam("Set-Password") @Size(min=4) String password,
			@NotNull Person personTemplate
	) {
		
		final EntityManager em = RestJpaLifecycleProvider.entityManager("messenger");
	
		final Person person;
		final boolean insertMode = personTemplate.getIdentity() == 0;
		if(insertMode) {
			person = new Person();
			
		}else{
			person = em.find(Person.class, personTemplate.getIdentity());
		}
		//get set data from person template into person
		
		person.getName().setSurname(personTemplate.getName().getSurname());
		person.getName().setForename(personTemplate.getName().getForename());
		person.setEmail(personTemplate.getEmail());
		person.getAddress().setStreet(personTemplate.getAddress().getStreet());
		person.getAddress().setCity(personTemplate.getAddress().getCity());
		person.getAddress().setPostCode(personTemplate.getAddress().getPostCode());
		person.setPasswordHash(HashTools.sha256HashCode(password));
		person.setVersion(personTemplate.getVersion());
		
		if(insertMode) {
			em.persist(person);
		}else{
			em.flush();
		}
		
		try {
			em.getTransaction().commit();
		}catch(RollbackException e){
			throw new ClientErrorException(Status.CONFLICT);
		}finally {
			em.getTransaction().begin();
		}
		em.close();
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
		
		final Person person = em.find(Person.class, identity); // TODO ! can find person with id 1/2
		if (person == null) throw new ClientErrorException(NOT_FOUND);

		return person; // TODO ! throws nullpointerexception even if a person (5) is found
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
	@Produces({MediaType.WILDCARD})
	public Document getAvatar (@HeaderParam(REQUESTER_IDENTITY) @Positive final long personIdentity, @QueryParam("width") @Nullable int width, 
			@QueryParam("height") @Nullable int height) {
		
		final EntityManager em = RestJpaLifecycleProvider.entityManager("messenger");
		final Document avatar = em.find(Person.class, personIdentity).getAvatar();
		byte[] content = avatar.getContent();
		if (content == null) throw new ClientErrorException(NOT_FOUND);
		if(width  > 0 && height > 0 ) {
			 content = Document.scaledImageContent(content, width, height);
		}
		return avatar; // TODO ! throws nullpointerexception even if a avatar (5) is found
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
	@Consumes({MediaType.WILDCARD})
	@Path("/{id}/avatar")
	public void updateAvatar (@HeaderParam(REQUESTER_IDENTITY) @Positive final long personIdentity, byte[] content) {
		
		final EntityManager em = RestJpaLifecycleProvider.entityManager("messenger");
		final Document avatar = em.find(Person.class, personIdentity).getAvatar();
		
		if (avatar == null) throw new ClientErrorException(NOT_FOUND);
		else {
			avatar.setContent(content);
			em.getTransaction().begin();
			em.persist(avatar);
			em.getTransaction().commit();
			em.flush();
		}
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
			@FormParam("password") String password,
			@FormParam("avatar") Document avatar) {
		
		Person person = new Person(avatar);
		person.getAddress().setCity(city);
		person.getAddress().setPostCode(postCode);
		person.getAddress().setStreet(street);
		person.getName().setForename(firstName);
		person.getName().setSurname(surName);
		person.setEmail(email);
		person.setPasswordHash(HashTools.sha256HashCode(password));
		
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
		person.getAddress().setCity(city);
		person.getAddress().setPostCode(postCode);
		person.getAddress().setStreet(street);
		person.getName().setForename(firstName);
		person.getName().setSurname(surName);
		person.setEmail(email);
		em.getTransaction().commit();
		em.flush();
	}
	
	
	
	@PUT
	@Consumes({ APPLICATION_JSON, APPLICATION_XML })
	@Produces({ APPLICATION_JSON, APPLICATION_XML })
	@Path("/{id}/peopleObserved")
	public void updatePeopleObserved(
			EntityManager em, 
			@HeaderParam(REQUESTER_IDENTITY) @Positive final long identity, 
			@FormParam("newObservedId") long newObservedId) {
		
		Person person = em.find(Person.class, identity);
		boolean exists = person.getPeopleObserved().stream().anyMatch(p -> p.getIdentity() == newObservedId);
		if(!exists) person.getPeopleObserved().add(em.find(Person.class, newObservedId));
		
		em.getTransaction().begin();
		em.persist(person);
		em.getTransaction().commit();
		em.flush();
	}
}
