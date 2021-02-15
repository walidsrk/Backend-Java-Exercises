package be.vinci.pae.api;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import be.vinci.pae.domain.PublicUser;
import be.vinci.pae.domain.User;
import be.vinci.pae.services.DataServiceUserCollection;
import be.vinci.pae.utils.Config;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;

import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

import jakarta.ws.rs.WebApplicationException;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Singleton
@Path("/auths")
public class Authentication {

	private final Algorithm jwtAlgorithm = Algorithm.HMAC256(Config.getProperty("JWTSecret"));
	private final ObjectMapper jsonMapper = new ObjectMapper();

	@POST
	@Path("login")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response login(JsonNode json) {
		// Get and check credentials
		if (!json.hasNonNull("login") || !json.hasNonNull("password")) {
			return Response.status(Status.UNAUTHORIZED).entity("Login and password needed").type(MediaType.TEXT_PLAIN)
					.build();
		}
		String login = json.get("login").asText();
		String password = json.get("password").asText();

		// Try to login
		User user = DataServiceUserCollection.getUser(login);
		if (user == null || !user.checkPassword(password)) {
			return Response.status(Status.UNAUTHORIZED).entity("Login or password incorrect").type(MediaType.TEXT_PLAIN)
					.build();
		}
		// Create token
		String token;
		try {
			token = JWT.create().withIssuer("auth0").withClaim("user", user.getID()).sign(this.jwtAlgorithm);
		} catch (Exception e) {
			throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR)
					.entity("Unable to create token").type("text/plain").build());
		}

		// Build response
		PublicUser publicUser = new PublicUser(user);
		ObjectNode node = jsonMapper.createObjectNode().put("token", token).putPOJO("user", publicUser);
		return Response.ok(node, MediaType.APPLICATION_JSON).build();

	}

	@POST
	@Path("register")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response register(JsonNode json) {
		// Get and check credentials
		if (!json.hasNonNull("login") || !json.hasNonNull("password")) {
			return Response.status(Status.UNAUTHORIZED).entity("Login and password needed").type(MediaType.TEXT_PLAIN)
					.build();
		}
		String login = json.get("login").asText();
		String password = json.get("password").asText();
		// Check if user exists
		if (DataServiceUserCollection.getUser(login) != null) {
			return Response.status(Status.CONFLICT).entity("This login is already in use").type(MediaType.TEXT_PLAIN)
					.build();
		}
		// create user
		User user = new User();
		user.setID(1);
		user.setLogin(login);
		user.setPassword(user.hashPassword(password));
		DataServiceUserCollection.addUser(user);

		// Create token
		String token;
		try {
			token = JWT.create().withIssuer("auth0").withClaim("user", user.getID()).sign(this.jwtAlgorithm);
		} catch (Exception e) {
			throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR)
					.entity("Unable to create token").type("text/plain").build());
		}
		// Build response

		PublicUser publicUser = new PublicUser(user);
		ObjectNode node = jsonMapper.createObjectNode().put("token", token).putPOJO("user", publicUser);
		return Response.ok(node, MediaType.APPLICATION_JSON).build();

	}

}
