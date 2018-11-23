package de.sb.messenger.rest;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.validation.constraints.Positive;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import de.sb.messenger.persistence.BaseEntity;
import de.sb.messenger.persistence.Message;
import de.sb.messenger.persistence.Person;
import de.sb.toolbox.net.RestJpaLifecycleProvider;

@Path("messages")
public class MessageService {


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
	public Message[] getMessages ( ) {
		final Message[] messages = null;
		
		final EntityManager em = RestJpaLifecycleProvider.entityManager("messenger");
		
		//messages = em.find();
		if (messages == null) throw new ClientErrorException(NOT_FOUND);

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
	@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Produces(MediaType.TEXT_PLAIN)
	public void setMessage( String body, Person author, BaseEntity subject) {
		
		final EntityManager em = RestJpaLifecycleProvider.entityManager("messenger");
		Message message = new Message(body, author, subject);
		
		em.getTransaction().begin();
		em.persist(message);
		em.getTransaction().commit();
	}
}
