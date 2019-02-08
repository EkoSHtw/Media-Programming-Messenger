package de.sb.messenger.rest;

import static de.sb.messenger.rest.BasicAuthenticationFilter.REQUESTER_IDENTITY;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;
import static javax.ws.rs.core.Response.Status.FORBIDDEN;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.Cache;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.RollbackException;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import com.sun.istack.internal.Nullable;
import de.sb.messenger.persistence.Document;
import de.sb.messenger.persistence.Group;
import de.sb.messenger.persistence.HashTools;
import de.sb.messenger.persistence.Person;
import de.sb.toolbox.net.RestJpaLifecycleProvider;


@Path("/people")
public class PersonService {
	static private final String QUERY_PEOPLE = "Select p.identity from Person as p where " + "(:surname is null or :surname = p.name.surname) and " + "(:forename is null or :forename = p.name.forename) and " + "(:email is null or :email = p.email) and " + "(:street is null or :street = p.address.street) and " + "(:postCode is null or :postCode = p.address.postCode) and " + "(:city is null or :city = p.address.city) and " + "(:groupAlias is null or :groupAlias = p.group)";
	static private final String QUERY_DOCUMENT = "Select p.identity from Document as p where :contentHash = p.contentHash";
	static private final Comparator<Person> PERSON_COMPARATOR = Comparator.comparing(Person::getName).thenComparing(Person::getEmail);

	/**
	 * Returning all people matching the given criteria.
	 * 
	 * @param surName
	 * @param firstName
	 * @param email
	 * @param street
	 * @param postCode
	 * @param city
	 * @param groupAlias
	 * @return the matching people (HTTP 200)
	 * @throws ClientErrorException  (HTTP 404) no people are found
	 * @throws PersistenceException  (HTTP 500) if there is a problem with the persistence layer
	 * @throws IllegalStateException (HTTP 500) if the entity manager associated with the current thread is not open
	 */
	@GET
	@Produces({ APPLICATION_JSON, APPLICATION_XML })
	public Person[] queryPeople(
			@QueryParam("resultOffset") @Positive int resultOffset, 
			@QueryParam("resultLimit") @Positive int resultLimit,
			@QueryParam("email") String email, 
			@QueryParam("surname") String surname, 
			@QueryParam("forename") String forename, 
			@QueryParam("street") String street, 
			@QueryParam("postCode") String postCode, 
			@QueryParam("city") String city, 
			@QueryParam("groupAlias") Group group
	) {
		final EntityManager em = RestJpaLifecycleProvider.entityManager("messenger");
		TypedQuery<Long> query = em.createQuery(QUERY_PEOPLE, Long.class);
		if (resultOffset > 0) query.setFirstResult(resultOffset);
		if (resultLimit > 0) query.setMaxResults(resultLimit);
		List<Long> peopleReferences = query.setParameter("surname", surname).setParameter("forename", forename).setParameter("email", email).setParameter("street", street).setParameter("postCode", postCode).setParameter("city", city).setParameter("groupAlias", group.name()).getResultList();

		final Person[] people = peopleReferences.stream().map(reference -> em.find(Person.class, reference)).filter(person -> person != null).sorted(PERSON_COMPARATOR).toArray(length -> new Person[length]);
		return people;
	}

	
	/**
	 * Creates or updates a person
	 * 
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
	 * @throws ClientErrorException  (HTTP 404) person is not found
	 * @throws PersistenceException  (HTTP 500) if there is a problem with the persistence layer
	 * @throws IllegalStateException (HTTP 500) if the entity manager associated with the current thread is not open
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces(MediaType.TEXT_PLAIN)
	public long modifyOrCreatePerson(
			@HeaderParam(REQUESTER_IDENTITY) @Positive final long requesterIdentity, 
			@HeaderParam("Set-Password") @Size(min = 4) String password, 
			@NotNull Person personTemplate
	) {
		final EntityManager em = RestJpaLifecycleProvider.entityManager("messenger");

		final Person person;
		final boolean insertMode = personTemplate.getIdentity() == 0;
		if (insertMode) {
			final Document avatar = em.find(Document.class, 1l);
			if (avatar == null) throw new ClientErrorException(Status.NOT_FOUND);
			person = new Person(avatar);
		} else {
			person = em.find(Person.class, personTemplate.getIdentity());
			if (person == null) throw new ClientErrorException(Status.NOT_FOUND);
		}

		person.getName().setSurname(personTemplate.getName().getSurname());
		person.getName().setForename(personTemplate.getName().getForename());
		person.setEmail(personTemplate.getEmail());
		person.getAddress().setStreet(personTemplate.getAddress().getStreet());
		person.getAddress().setCity(personTemplate.getAddress().getCity());
		person.getAddress().setPostCode(personTemplate.getAddress().getPostCode());
		person.setVersion(personTemplate.getVersion());
		if (password != null) person.setPasswordHash(HashTools.sha256HashCode(password));

		if (insertMode) {
			em.persist(person);
		} else {
			em.flush();
		}

		try {
			em.getTransaction().commit();
		} catch (RollbackException e) {
			throw new ClientErrorException(Status.CONFLICT);
		} finally {
			em.getTransaction().begin();
		}

		return person.getIdentity();
	}

	
	/**
	 * Returns the person with the given identity.
	 * 
	 * @param personIdentity the person identity
	 * @return the matching person (HTTP 200)
	 * @throws ClientErrorException  (HTTP 404) if the given person cannot be found
	 * @throws PersistenceException  (HTTP 500) if there is a problem with the persistence layer
	 * @throws IllegalStateException (HTTP 500) if the entity manager associated with the current thread is not open
	 */
	@GET
	@Path("/{id}")
	@Produces({ APPLICATION_JSON, APPLICATION_XML })
	public Person findPerson(
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
	 * 
	 * @param personIdentity the person identity
	 * @return the document for the person (HTTP 200)
	 * @throws ClientErrorException  (HTTP 404) if the given avatar cannot be found
	 * @throws PersistenceException  (HTTP 500) if there is a problem with the persistence layer
	 * @throws IllegalStateException (HTTP 500) if the entity manager associated with the current thread is not open
	 */
	@GET
	@Path("/{id}/avatar")
	@Produces("image/*")
	public Response getAvatar(
			@HeaderParam(REQUESTER_IDENTITY) @Positive final long personIdentity, 
			@QueryParam("width") @Nullable int width, 
			@QueryParam("height") @Nullable int height
	) {
		final EntityManager em = RestJpaLifecycleProvider.entityManager("messenger");
		final Document avatar = em.find(Person.class, personIdentity).getAvatar();
		if (avatar == null) throw new ClientErrorException(NOT_FOUND);
		byte[] content = avatar.getContent();
		if (width > 0 && height > 0) content = Document.scaledImageContent(content, width, height);
		return Response.ok(content, avatar.getContentType()).build();
	}

	/**
	 * Updates the avatar content of the person matching the given identity
	 * 
	 * @param personIdentity the person identity
	 * @return the document for the person (HTTP 200)
	 * @throws ClientErrorException  (HTTP 404) if the given person cannot be found
	 * @throws PersistenceException  (HTTP 500) if there is a problem with the persistence layer
	 * @throws IllegalStateException (HTTP 500) if the entity manager associated with the current thread is not open
	 */
	// produces text plain
	@PUT
	@Consumes("image/*")
	@Produces({ MediaType.TEXT_PLAIN })
	@Path("/{id}/avatar")
	public long updateAvatar(
			@HeaderParam(REQUESTER_IDENTITY) @Positive final long requesterIdentity, 
			@HeaderParam("Content-Type") @NotNull final String contentType, 
			@PathParam("id") @Positive final long personIdentity, 
			@NotNull byte[] content
	) {
		if (requesterIdentity != personIdentity) throw new ClientErrorException(FORBIDDEN);
		final EntityManager em = RestJpaLifecycleProvider.entityManager("messenger");

		final byte[] hash = HashTools.sha256HashCode(content);
		final TypedQuery<Long> query = em.createQuery(QUERY_DOCUMENT, Long.class);
		final int avatarId = query.setParameter("hashCode", hash).getFirstResult();

		final Document avatar;
		if (avatarId > 0) {
			avatar = em.find(Document.class, avatarId);
			if (avatar == null) throw new ClientErrorException(NOT_FOUND);
		} else {
			avatar = new Document();
			avatar.setContent(content);
		}

		avatar.setContentType(contentType);
		if (avatarId > 0) em.flush(); else em.persist(avatar);

		try {
			em.getTransaction().commit();
		} catch (RollbackException e) {
			throw new ClientErrorException(Status.CONFLICT);
		} finally {
			em.getTransaction().begin();
		}

		Person p = em.find(Person.class, personIdentity);
		if (p == null) throw new ClientErrorException(NOT_FOUND);

		p.setAvatar(avatar);
		em.flush();

		try {
			em.getTransaction().commit();
		} catch (RollbackException e) {
			throw new ClientErrorException(Status.CONFLICT);
		} finally {
			em.getTransaction().begin();
		}

		return avatar.getIdentity();
	}

	
	@PUT
	@Path("/{id}/peopleObserved")
	public void updatePeopleObserved(
			@HeaderParam(REQUESTER_IDENTITY) @Positive final long requesterIdentity, 
			@PathParam("id") @Positive final long personIdentity,
			@QueryParam("observedReference") final Set<Long> observedReferences
	) {
		if (requesterIdentity != personIdentity) throw new ClientErrorException(FORBIDDEN);
		final EntityManager em = RestJpaLifecycleProvider.entityManager("messenger");
		
		final Person person = em.find(Person.class, personIdentity);
		if (person == null) throw new ClientErrorException(NOT_FOUND);
		
		final Set<Long> existingReferences = person.getPeopleObserved().stream().map(Person::getIdentity).collect(Collectors.toSet());
		final Set<Long> additionalReferences = new HashSet<>( observedReferences );
		additionalReferences.removeAll(existingReferences);
		final Set<Long> superflousReferences = new HashSet<>( observedReferences );
		superflousReferences.retainAll(existingReferences);
		
		final Set<Person> observedPeople = observedReferences.stream().map(reference -> em.find(Person.class, reference)).filter(p -> p != null).collect(Collectors.toSet());
		person.getPeopleObserved().clear();
		person.getPeopleObserved().addAll(observedPeople);
		em.flush();

		try {
			em.getTransaction().commit();
		} catch (RollbackException e) {
			throw new ClientErrorException(Status.CONFLICT);
		} finally {
			em.getTransaction().begin();
		}

		final Cache cache = em.getEntityManagerFactory().getCache();
		cache.evict(Person.class, personIdentity);
		for (long reference : additionalReferences) 
			cache.evict(Person.class, reference);
		for (long reference : superflousReferences) 
			cache.evict(Person.class, reference);
	}
}
