package com.gsnotes.services.impl;

import java.util.List;

import org.passay.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gsnotes.bo.Compte;
import com.gsnotes.bo.Etudiant;
import com.gsnotes.bo.InscriptionAnnuelle;
import com.gsnotes.bo.Niveau;
import com.gsnotes.bo.Role;
import com.gsnotes.bo.Utilisateur;
import com.gsnotes.dao.ICompteDao;
import com.gsnotes.dao.IInscriptionAnnuelleDao;
import com.gsnotes.dao.IRoleDao;
import com.gsnotes.dao.IUtilisateurDao;
import com.gsnotes.services.ICompteService;
import com.gsnotes.services.IInscriptionAnnuelleService;
import com.gsnotes.utils.export.ExcelExporter;

@Service
@Transactional
public class IInscriptionAnnuelleServiceImpl implements IInscriptionAnnuelleService {

	@Autowired
	IInscriptionAnnuelleDao inscr;

	@Override
	public InscriptionAnnuelle getInscription(Niveau niv, Etudiant et) {
		return inscr.getByNiveauAndEtudiant(niv, et);
	}

	@Override
	public void AjouterInscription(InscriptionAnnuelle ins) {
		
		inscr.save(ins);
	}

	@Override
	public List<InscriptionAnnuelle> getAll(Etudiant et) {
		return inscr.getByEtudiant(et);
	}

	@Override
	public void setAll(List<InscriptionAnnuelle> in) {

		for(InscriptionAnnuelle i : in) {
			inscr.save(i);
		}
	}



	

}
