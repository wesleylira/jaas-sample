package br.com.java.samples.jaas;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.junit.Assert.assertThat;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import br.com.java.samples.jaas.model.Role;
import br.com.java.samples.jaas.model.User;
import br.com.java.samples.jaas.repository.UserRepository;
import br.com.java.samples.jaas.rest.UserService;
import br.com.java.samples.jaas.util.Resources;

@RunWith(Arquillian.class)
public class UserServiceTest {
	
	@Deployment
	public static Archive<?> createTestArchive() {
		return ShrinkWrap
				.create(WebArchive.class, "test.war")
				.addClasses(User.class, Role.class, UserRepository.class, UserService.class, Resources.class)
				.addAsResource("META-INF/persistence.xml")
				.addAsWebInfResource("arquillian-ds.xml").addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
	}
	
	@Inject
	private UserService service;
	
	private User user = new User("any_login", "any_pass", "admin", "manager");
	
	@Test
	@InSequence(1)
	public void shouldRegisterANewUser() {
		Response response = service.save(user);
		
		assertThat(response.getStatus(), is(Response.Status.OK.getStatusCode()));
	}
	
	@Test
	@InSequence(2)
	public void shouldRetriveTheRegisteredUser() {
		List<User> users = service.list();
		
		assertThat(users, hasItem(user));
	}
	
}