package be.vinci.pae.domain;

public class Page {
	
	private int id;
	private String titre;
	private String URL;
	private String contenu;
	private int auteur;
	private final static String [] STATUS_PUBLICATION = {"hidden","published"};
	private String status;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitre() {
		return titre;
	}
	public void setTitre(String titre) {
		this.titre = titre;
	}
	public String getURL() {
		return URL;
	}
	public void setURL(String uRL) {
		URL = uRL;
	}
	public String getContenu() {
		return contenu;
	}
	public void setContenu(String contenu) {
		this.contenu = contenu;
	}
	public int getAuteur() {
		return auteur;
	}
	public void setAuteur(int auteur) {
		this.auteur = auteur;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "Page [id=" + id + ", titre=" + titre + ", URL=" + URL + ", contenu=" + contenu + ", auteur=" + auteur
				+ ", status=" + status + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Page other = (Page) obj;
		if (id != other.id)
			return false;
		return true;
	}
	

	
	
}
