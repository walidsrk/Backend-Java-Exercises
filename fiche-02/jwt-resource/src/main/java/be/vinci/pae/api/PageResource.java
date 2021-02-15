package be.vinci.pae.api;

import java.rmi.AccessException;
import java.util.List;

import org.glassfish.jersey.inject.hk2.RequestContext;
import org.glassfish.jersey.server.ContainerRequest;

import be.vinci.pae.api.filters.Authorize;
import be.vinci.pae.domain.Page;
import be.vinci.pae.domain.User;
import be.vinci.pae.services.DataServicePageCollection;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Singleton
@Path("/pages")
public class PageResource {

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Authorize
	public Page create(Page page) {
		if (page == null || page.getTitre() == null || page.getTitre().isEmpty() || page.getURL() == null 
			|| page.getURL().isEmpty())
			throw new WebApplicationException(
					Response.status(Status.BAD_REQUEST).entity("Lacks of mandatory info").type("text/plain").build());
		DataServicePageCollection.addPage(page);
		return page;
	}

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Authorize
	public Page getPage(@PathParam("id") int id,@Context ContainerRequest request) {
		if (id == 0)
			throw new WebApplicationException(Response.status(Status.BAD_REQUEST).entity("Lacks of mandatory id info")
					.type("text/plain").build());
		Page pageFound = DataServicePageCollection.getPage(id);
		User user = (User) request.getProperty("user");
		if (pageFound == null)
			throw new WebApplicationException(Response.status(Status.NOT_FOUND)
					.entity("Ressource with id = " + id + " could not be found").type("text/plain").build());
		if(pageFound.getStatus().equals("hidden") && user.getID()!=pageFound.getAuteur())
			throw new WebApplicationException(Response.status(Status.FORBIDDEN).type("text/plain").build());
		return pageFound;
	}

	@PUT
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Authorize
	public Page updatePage(Page page, @PathParam("id") int id, @Context ContainerRequest request) {

		if (page == null || page.getTitre() == null || page.getTitre().isEmpty() || page.getURL() == null 
				|| page.getURL().isEmpty() || page.getStatus() == null 
				|| page.getStatus().isEmpty())
			throw new WebApplicationException(
					Response.status(Status.BAD_REQUEST).entity("Lacks of mandatory info").type("text/plain").build());
		User user = (User) request.getProperty("user");
		if(user.getID() != page.getAuteur()) {
			throw new WebApplicationException(Response.status(Status.FORBIDDEN).type("text/plain").build());
		}
		if(!page.getStatus().equals("hidden") && !page.getStatus().equals("published"))
			throw new WebApplicationException(Response.status(Status.BAD_REQUEST)
					.entity("illegal argument exception").type("text/plain").build());
		page.setId(id);
		Page updatedPage = DataServicePageCollection.updatePage(page);
		if (updatedPage == null)
			throw new WebApplicationException(Response.status(Status.NOT_FOUND)
					.entity("Ressource with id = " + id + " could not be found").type("text/plain").build());

		return updatedPage;
	}

	@DELETE
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Authorize
	public Page deletePage(@PathParam("id") int id, @Context ContainerRequest request) {
		if (id == 0)
			throw new WebApplicationException(Response.status(Status.BAD_REQUEST).entity("Lacks of mandatory id info")
					.type("text/plain").build());

		User user = (User) request.getProperty("user");
		Page pageFound = getPage(id, request);
		if(user.getID() != pageFound.getAuteur()) {
			throw new WebApplicationException(Response.status(Status.FORBIDDEN).type("text/plain").build());
		}
		Page deletedPage = DataServicePageCollection.deletePage(id);
		if (deletedPage == null)
			throw new WebApplicationException(Response.status(Status.NOT_FOUND)
					.entity("Ressource with id = " + id + " could not be found").type("text/plain").build());

		return deletedPage;
	}

}
