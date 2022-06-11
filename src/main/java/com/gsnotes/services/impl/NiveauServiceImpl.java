package com.gsnotes.services.impl;

import java.util.List;

import org.passay.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gsnotes.bo.Compte;
import com.gsnotes.bo.Etudiant;
import com.gsnotes.bo.Niveau;
import com.gsnotes.bo.Role;
import com.gsnotes.bo.Utilisateur;
import com.gsnotes.dao.ICompteDao;
import com.gsnotes.dao.IEtudiantDao;
import com.gsnotes.dao.INiveauDao;
import com.gsnotes.dao.IRoleDao;
import com.gsnotes.dao.IUtilisateurDao;
import com.gsnotes.services.ICompteService;
import com.gsnotes.services.IEtudiantService;
import com.gsnotes.services.INiveauService;
import com.gsnotes.utils.export.ExcelExporter;

@Service
@Transactional
public class NiveauServiceImpl implements INiveauService {

	@Autowired
	INiveauDao niv;

	@Override
	public Niveau getNiveau(Long id) {
		return niv.getById(id);
	}
	

	

}
