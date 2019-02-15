package de.sb.messenger.rest;

import static de.sb.messenger.rest.BasicAuthenticationFilter.REQUESTER_IDENTITY;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import de.sb.messenger.persistence.BaseEntity;
import de.sb.messenger.persistence.Message;
import de.sb.messenger.persistence.Person;
import de.sb.toolbox.net.RestJpaLifecycleProvider;

@Path("messages")
public class MessageService {


	static private final String QUERY_MESSAGES = "Select m.identity from Message as m where "
			+ "(:body = m.body) and "
			+ "(:lowerCreationTimestamp <= m.creationTimestamp) and "
			+ "(:upperCreationTimestamp >= m.creationTimestamp)";
	
	/**
	 * Returning all messages matching the given criteria.
	 * @param 
	 * @return the matching messages (HTTP 200)
	 * @throws ClientErrorException (HTTP 404) no messages are found
	 * @throws PersistenceException (HTTP 500) if there is a problem with the persistence layer
	 * @throws IllegalStateException (HTTP 500) if the entity manager associated with the current thread is not open
	 */
	@GET
	@Produces({ APPLICATION_JSON, APPLICATION_XML })
	public Message[] queryMessages (
			@QueryParam("resultOffSet") @PositiveOrZero int resultOffSet,
			@QueryParam("resultLimit") @PositiveOrZero int resultLimit,
			@QueryParam("body") String body, 
			@QueryParam("lowerCreationTimestamp") Long lowerCreationTimestamp,
			@QueryParam("upperCreationTimestamp") Long upperCreationTimestamp
	) {
		final EntityManager em = RestJpaLifecycleProvider.entityManager("messenger");
		final TypedQuery<Long> query = em.createQuery(QUERY_MESSAGES, Long.class);
		if (resultOffSet > 0) query.setFirstResult(resultOffSet);
		if (resultLimit > 0) query.setMaxResults(resultLimit);
		
		final Message[] messages = query
				.setParameter("body", body)
				.setParameter("lowerCreationTimestamp", lowerCreationTimestamp)
				.setParameter("upperCreationTimestamp", upperCreationTimestamp)
				.getResultList()
				.stream()
				.map(identity -> em.find(Message.class, identity))
				.filter(message -> message != null)
				.sorted()
				.toArray(length -> new Message[length]);
		return messages;
	}
	
	
	
	
	
	/**
	 * Returns the message with the given identity.
	 * @param messageIdentity the message identity
	 * @return the matching message (HTTP 200)
	 * @throws ClientErrorException (HTTP 404) if the given message cannot be found
	 * @throws PersistenceException (HTTP 500) if there is a problem with the persistence layer
	 * @throws IllegalStateException (HTTP 500) if the entity manager associated with the current thread is not open
	 */
	@GET
	@Path("{id}")
	@Produces({ APPLICATION_JSON, APPLICATION_XML })
	public Message getMessage ( @PathParam("id") @Positive final long messageIdentity ) {
		
		final EntityManager em = RestJpaLifecycleProvider.entityManager("messenger");
		final Message message = em.find(Message.class, messageIdentity);
		if (message == null) throw new ClientErrorException(NOT_FOUND);

		return message;
	}
	
	
	
	/**
	 * Creates a new message
	 * @param body
	 * @param author
	 * @return the matching people (HTTP 200)
	 * @throws ClientErrorException (HTTP 404) person is not found
	 * @throws PersistenceException (HTTP 500) if there is a problem with the persistence layer
	 * @throws IllegalStateException (HTTP 500) if the entity manager associated with the current thread is not open
	 */
	@POST
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.TEXT_PLAIN)
	public long createMessage ( 
			@HeaderParam(REQUESTER_IDENTITY) @Positive final long requesterIdentity, 
			@QueryParam("subjectReference") @Positive final long subjectReference,
			@NotNull String body
	) {
		final EntityManager em = RestJpaLifecycleProvider.entityManager("messenger");
		final Person person = em.find(Person.class, requesterIdentity);
		if (person == null) throw new ClientErrorException(NOT_FOUND);
		final BaseEntity be = em.find(BaseEntity.class, subjectReference);
		if (be == null) throw new ClientErrorException(NOT_FOUND);
		
		Message message = new Message(person, be);
		message.setBody(body);
		em.persist(message);
		
		try {
			em.getTransaction().commit();
		} finally {
			em.getTransaction().begin();
		}
		return message.getIdentity();
	}
}
