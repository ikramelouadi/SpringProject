package com.gsnotes.web.models;

public class ExcelD {
	
	private long idEtudiant = -1;
	private String cneEtudiant = null;
	private String nomEtudiant = null;
	private String prenomEtudiant = null;
	private long idNiveau = -1;
	private String typeInscription = null;
	private String ancienCneEtudiant = null;
	private String ancienNomEtudiant = null;
	private String ancienPrenomEtudiant = null;
	private boolean update = false;
	
	public ExcelD() {
		super();
	}

	public long getIdEtudiant() {
		return idEtudiant;
	}

	public void setIdEtudiant(long idEtudiant) {
		this.idEtudiant = idEtudiant;
	}

	public String getCneEtudiant() {
		return cneEtudiant;
	}

	public void setCneEtudiant(String cneEtudiant) {
		this.cneEtudiant = cneEtudiant;
	}

	public String getNomEtudiant() {
		return nomEtudiant;
	}

	public void setNomEtudiant(String nomEtudiant) {
		this.nomEtudiant = nomEtudiant;
	}

	public String getPrenomEtudiant() {
		return prenomEtudiant;
	}

	public void setPrenomEtudiant(String prenomEtudiant) {
		this.prenomEtudiant = prenomEtudiant;
	}

	public long getIdNiveau() {
		return idNiveau;
	}

	public void setIdNiveau(long idNiveau) {
		this.idNiveau = idNiveau;
	}

	public String getTypeInscription() {
		return typeInscription;
	}

	public void setTypeInscription(String typeInscription) {
		this.typeInscription = typeInscription;
	}

	public String getAncienCneEtudiant() {
		return ancienCneEtudiant;
	}

	public void setAncienCneEtudiant(String ancienCneEtudiant) {
		this.ancienCneEtudiant = ancienCneEtudiant;
	}

	public String getAncienNomEtudiant() {
		return ancienNomEtudiant;
	}

	public void setAncienNomEtudiant(String ancienNomEtudiant) {
		this.ancienNomEtudiant = ancienNomEtudiant;
	}

	public String getAncienPrenomEtudiant() {
		return ancienPrenomEtudiant;
	}

	public void setAncienPrenomEtudiant(String ancienPrenomEtudiant) {
		this.ancienPrenomEtudiant = ancienPrenomEtudiant;
	}

	public boolean isUpdate() {
		return update;
	}

	public void setUpdate(boolean update) {
		this.update = update;
	}
}