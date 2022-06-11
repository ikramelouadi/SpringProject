package com.gsnotes.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gsnotes.bo.Compte;
import com.gsnotes.bo.Etudiant;
import com.gsnotes.bo.InscriptionAnnuelle;
import com.gsnotes.bo.Niveau;

public interface IInscriptionAnnuelleDao extends JpaRepository<InscriptionAnnuelle, Long> {
	public InscriptionAnnuelle getByNiveauAndEtudiant(Niveau niv,Etudiant et);
	public List<InscriptionAnnuelle> getByEtudiant(Etudiant et);

}
