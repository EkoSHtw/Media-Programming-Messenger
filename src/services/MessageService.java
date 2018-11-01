package services;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.validation.constraints.Positive;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import de.sb.toolbox.net.RestJpaLifecycleProvider;
import entities.Message;

@Path("messages")
public class MessageService {

	
	/**
	 * Returns the message with the given identity.
	 * @param messageIdentity the message identity
	 * @return the matching entity (HTTP 200)
	 * @throws ClientErrorException (HTTP 404) if the given message cannot be found
	 * @throws PersistenceException (HTTP 500) if there is a problem with the persistence layer
	 * @throws IllegalStateException (HTTP 500) if the entity manager associated with the current thread is not open
	 */
	@GET
	@Path("{id}")
	@Produces({ APPLICATION_JSON, APPLICATION_XML })
	public Message queryEntity ( @PathParam("id") @Positive final long messageIdentity ) {
		
		final EntityManager messengerManager = RestJpaLifecycleProvider.entityManager("messenger");
		final Message message = messengerManager.find(Message.class, messageIdentity);
		if (message == null) throw new ClientErrorException(NOT_FOUND);

		return message;
	}
}
