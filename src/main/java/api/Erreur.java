package api;

public class Erreur {
	private String descriptionErreur;
	
	public Erreur(String x) {
		descriptionErreur=x;
	}

	public String getDescriptionErreur() {
		return descriptionErreur;
	}

	public void setDescriptionErreur(String descriptionErreur) {
		this.descriptionErreur = descriptionErreur;
	}
	
}
