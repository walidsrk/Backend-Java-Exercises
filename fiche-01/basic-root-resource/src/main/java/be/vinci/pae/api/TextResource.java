package be.vinci.pae.api;

import java.util.List;

import be.vinci.pae.domain.Film;
import be.vinci.pae.domain.Text;
import be.vinci.pae.services.DataServiceTextCollection;

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
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Singleton
@Path("/texts")
public class TextResource {	

    /*
        READ ALL : Lire toutes les ressources de la collection 
        + READ ALL FILTERED : Lire toutes les ressources de 
        la collection selon le filtre donné
    */
    @GET	
	@Produces(MediaType.APPLICATION_JSON)
	public List<Text> getAllTexts(@QueryParam("level") String level) {
		if (level != null)
			return DataServiceTextCollection.getTexts(level);
		return DataServiceTextCollection.getTexts();

	}  

    /*
        READ ONE : Lire la ressource identifiée
    */
    @GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Text getText(@PathParam("id") int id) {
		if (id == 0)
			throw new WebApplicationException(Response.status(Status.BAD_REQUEST).entity("Lacks of mandatory id info")
					.type("text/plain").build());
		Text TextFound = DataServiceTextCollection.getText(id);

		if (TextFound == null)
			throw new WebApplicationException(Response.status(Status.NOT_FOUND)
					.entity("Ressource with id = " + id + " could not be found").type("text/plain").build());

		return TextFound;
	}

    /*
        CREATE ONE : Créer une ressource basée 
        sur les données de la requête
    */
    @POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Text create(Text text) {
		if (text == null || text.getContent() == null || text.getContent().isEmpty() || text.getLevel() == null || text.getLevel().isEmpty())
			throw new WebApplicationException(
					Response.status(Status.BAD_REQUEST).entity("Lacks of mandatory info").type("text/plain").build());
		DataServiceTextCollection.addText(text);

		return text;
	}

    /*
        DELETE ONE : Effacer la ressource identifiée
    */
	@DELETE
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Text deleteText(@PathParam("id") int id) {
		if (id == 0)
			throw new WebApplicationException(Response.status(Status.BAD_REQUEST).entity("Lacks of mandatory id info")
					.type("text/plain").build());

		Text deletedText = DataServiceTextCollection.deleteText(id);

		if (deletedText == null)
			throw new WebApplicationException(Response.status(Status.NOT_FOUND)
					.entity("Ressource with id = " + id + " could not be found").type("text/plain").build());

		return deletedText;
	}

    /*
        UPDATE ONE : Remplacer l’entièreté
        de la ressource par les données de la requête
    */
	@PUT
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Text updateText(Text text, @PathParam("id") int id) {

		if (text == null || text.getContent() == null || text.getContent().isEmpty() || text.getLevel() == null || text.getLevel().isEmpty())
			throw new WebApplicationException(
					Response.status(Status.BAD_REQUEST).entity("Lacks of mandatory info").type("text/plain").build());

		text.setId(id);
		Text updatedText = DataServiceTextCollection.updateText(text);

		if (updatedText == null)
			throw new WebApplicationException(Response.status(Status.NOT_FOUND)
					.entity("Ressource with id = " + id + " could not be found").type("text/plain").build());

		return updatedText;
	}

	

	

}
