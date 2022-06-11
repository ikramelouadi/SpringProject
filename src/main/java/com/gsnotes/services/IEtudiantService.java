package com.gsnotes.services;

import java.util.List;

import com.gsnotes.bo.Compte;
import com.gsnotes.bo.Etudiant;
import com.gsnotes.bo.Role;
import com.gsnotes.utils.export.ExcelExporter;


public interface IEtudiantService {
	
	public Etudiant getEtudiantByCne(String cne);

	public Etudiant save(Etudiant et);
}
