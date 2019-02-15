package de.sb.messenger.rest;

import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.client.WebTarget;
import org.junit.Assert;
import org.junit.Test;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

public class PersonServiceTest extends ServiceTest {
	
	@Test
	public void testFindPeople() {
		
		final WebTarget webTarget = newWebTarget("ines.bergmann@web.de", "ines");
		webTarget.queryParam("resultOffset", "0");
		webTarget.queryParam("resultLimit", "20");
		webTarget.queryParam("email", "ines.bergmann@web.de");
		final String response =
				webTarget.path("/people")
	                    .request(APPLICATION_JSON)
	                    .get(String.class);

//	    final JsonObject result = Json.parse(response).asObject();
	    
	    Assert.assertNotEquals(response, null);
//	    Assert.assertEquals(2, response.size());
		
//		final EntityManager em = getEntityManagerFactory().createEntityManager();
		
//		Person p = new Person();
//		p.getName().setForename("ForeName");
//		p.getName().setSurname("SurName");
//		em.getTransaction().begin();
//		em.persist(p);
//		em.getTransaction().commit();
		
//		Person p2 = em.find(Person.class, p.getIdentity());
		
//		assertEquals("V2", p2.getName().getForename(), "Succcesfull");
		
	}
	
}
