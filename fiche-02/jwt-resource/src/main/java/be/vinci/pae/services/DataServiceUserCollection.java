package be.vinci.pae.services;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import be.vinci.pae.domain.PublicUser;
import be.vinci.pae.domain.User;
import be.vinci.pae.utils.Config;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.ext.Provider;

public class DataServiceUserCollection {

	private static final String DB_FILE_PATH = Config.getProperty("DatabaseFilePath");
	private static final String COLLECTION_NAME = "users";
	private final static ObjectMapper jsonMapper = new ObjectMapper();

	private static List<User> users;
	static {
		users = loadDataFromFile();
	}

	public static User getUser(String login) {
		return users.stream().filter(u -> u.getLogin().equals(login)).findAny().orElse(null);
	}

	public static User getUser(int id) {
		return users.stream().filter(u -> u.getID() == id).findAny().orElse(null);
	}

	public static List<User> getUsers() {
		return users;
	}

	public static List<PublicUser> getPublicUsers() {
		return users.stream().map(user -> new PublicUser(user)).collect(Collectors.toList());

	}

	public static User addUser(User user) {
		user.setID(users.size() + 1);
		users.add(user);
		saveDataToFile();
		return user;
	}

	private static List<User> loadDataFromFile() {
		try {
			JsonNode node = jsonMapper.readTree(Paths.get(DB_FILE_PATH).toFile());
			JsonNode collection = node.get(COLLECTION_NAME);
			if (collection == null)
				return new ArrayList<User>();
			return jsonMapper.readerForListOf(User.class).readValue(node.get(COLLECTION_NAME));

		} catch (FileNotFoundException e) {
			return new ArrayList<User>();
		} catch (IOException e) {
			e.printStackTrace();
			return new ArrayList<User>();
		}
	}

	private static void saveDataToFile() {
		try {

			// get all collections
			Path pathToDb = Paths.get(DB_FILE_PATH);
			if (!Files.exists(pathToDb)) {
				// write a new collection to the db file
				ObjectNode newCollection = jsonMapper.createObjectNode().putPOJO(COLLECTION_NAME, users);
				jsonMapper.writeValue(pathToDb.toFile(), newCollection);
				return;

			}
			// get all collections
			JsonNode allCollections = jsonMapper.readTree(pathToDb.toFile());

			if (allCollections.has(COLLECTION_NAME)) {// remove current collection
				((ObjectNode) allCollections).remove(COLLECTION_NAME);
			}

			// create a new JsonNode and add it to allCollections
			String currentCollectionAsString = jsonMapper.writeValueAsString(users);
			JsonNode updatedCollection = jsonMapper.readTree(currentCollectionAsString);
			((ObjectNode) allCollections).putPOJO(COLLECTION_NAME, updatedCollection);

			// write to the db file
			jsonMapper.writeValue(pathToDb.toFile(), allCollections);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
