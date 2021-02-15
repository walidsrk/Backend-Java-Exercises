package be.vinci.pae.api;

import java.util.List;

import org.glassfish.jersey.server.ContainerRequest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import be.vinci.pae.api.filters.Authorize;
import be.vinci.pae.domain.PublicUser;
import be.vinci.pae.domain.User;

import be.vinci.pae.services.DataServiceUserCollection;
import jakarta.inject.Singleton;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Singleton
@Path("/users")
public class UserResource {
	private static final ObjectMapper jsonMapper = new ObjectMapper();

	@POST
	@Path("init")
	@Produces(MediaType.APPLICATION_JSON)
	public PublicUser init() {
		User user = new User();
		user.setID(1);
		user.setLogin("james");
		user.setPassword(user.hashPassword("password"));
		DataServiceUserCollection.addUser(user);
		// JSON from POJO to provide only public data (password shall not be provided)
		return new PublicUser(user);
	}

	@GET
	@Path("me")
	@Produces(MediaType.APPLICATION_JSON)
	@Authorize
	public Response getUser(@Context ContainerRequest request) {
		User currentUser = (User) request.getProperty("user");
		PublicUser publicUser = new PublicUser(currentUser);
		// create a new JsonNode and add it to the response to filter public data
		// (deserialize / reserialize)
		try {
			String publicUserAsString = jsonMapper.writeValueAsString(publicUser);
			JsonNode publicUserAsNode = jsonMapper.readTree(publicUserAsString);
			return Response.ok(publicUserAsNode, MediaType.APPLICATION_JSON).build();
		} catch (JsonProcessingException e) {
			throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR)
					.entity("Unable to serialize user").type("text/plain").build());
		}
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Authorize
	public List<PublicUser> getAllUsers() {
		return DataServiceUserCollection.getPublicUsers();
	}

}
