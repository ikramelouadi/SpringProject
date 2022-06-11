package com.gsnotes.services;

import java.util.List;

import com.gsnotes.bo.Compte;
import com.gsnotes.bo.Etudiant;
import com.gsnotes.bo.InscriptionAnnuelle;
import com.gsnotes.bo.Niveau;
import com.gsnotes.bo.Role;
import com.gsnotes.utils.export.ExcelExporter;


public interface IInscriptionAnnuelleService {
	
	public InscriptionAnnuelle getInscription(Niveau niv,Etudiant et);
	
	
	public void AjouterInscription(InscriptionAnnuelle ins);

	List<InscriptionAnnuelle> getAll(Etudiant et);
	
	public void setAll(List<InscriptionAnnuelle> in);
	
	//public void AjouterReinscription(InscriptionAnnuelle ins);
}
