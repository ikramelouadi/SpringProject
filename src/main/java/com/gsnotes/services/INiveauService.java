package com.gsnotes.services;

import java.util.List;

import com.gsnotes.bo.Compte;
import com.gsnotes.bo.Etudiant;
import com.gsnotes.bo.Niveau;
import com.gsnotes.bo.Role;
import com.gsnotes.utils.export.ExcelExporter;


public interface INiveauService {
	
	public Niveau getNiveau(Long id);
}
