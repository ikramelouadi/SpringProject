package com.gsnotes.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gsnotes.bo.Compte;
import com.gsnotes.bo.Etudiant;

public interface IEtudiantDao extends JpaRepository<Etudiant, Long> {
	public Etudiant getByCne(String cne);

}
