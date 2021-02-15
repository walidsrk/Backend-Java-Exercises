package be.vinci.pae.domain;

public class PublicUser {

	private int id;

	private String login;

	public PublicUser(int id, String login) {

		this.id = id;
		this.login = login;
	}

	public PublicUser(User user) {
		this.id = user.getID();
		this.login = user.getLogin();
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public int getID() {
		return id;
	}

	public void setID(int id) {
		this.id = id;
	}

}
