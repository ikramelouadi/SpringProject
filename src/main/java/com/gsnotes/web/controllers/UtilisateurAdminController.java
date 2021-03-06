package com.gsnotes.web.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.gsnotes.bo.CadreAdministrateur;
import com.gsnotes.bo.Compte;
import com.gsnotes.bo.Enseignant;
import com.gsnotes.bo.Etudiant;
import com.gsnotes.bo.InscriptionAnnuelle;
import com.gsnotes.bo.Niveau;
import com.gsnotes.bo.Utilisateur;
import com.gsnotes.services.ICompteService;
import com.gsnotes.services.IEtudiantService;
import com.gsnotes.services.IInscriptionAnnuelleService;
import com.gsnotes.services.INiveauService;
import com.gsnotes.services.IPersonService;
import com.gsnotes.utils.export.ExcelExporter;
import com.gsnotes.utils.export.ExcelHandler;
import com.gsnotes.utilt.importt.Excel_Importer;
import com.gsnotes.web.models.PersonModel;
import com.gsnotes.web.models.UserAndAccountInfos;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

/**
 * Ce controleur g??re les personnes de type Etudiant, Enseignant et Cadre Admin
 * 
 * @author Boudaa
 *
 */

@Controller
@RequestMapping("/admin")
public class UtilisateurAdminController {

	@Autowired
	private IPersonService personService;

	@Autowired
	private HttpSession httpSession;
	
	@Autowired
	private IEtudiantService etSer;
	
	@Autowired
	private INiveauService nivSer;
	
	@Autowired
	private IInscriptionAnnuelleService inscSer;
	
	
	/** Utilis?? pour la journalisation */
	private Logger LOGGER = LoggerFactory.getLogger(getClass());


	public UtilisateurAdminController() {


		
		
	}

	@RequestMapping(value = "showForm", method = RequestMethod.GET)
	public String showForm(@RequestParam int typePerson, Model model) {

		PersonModel pmodel = new PersonModel(typePerson);
		model.addAttribute("personModel", pmodel);

		// Nous avons choisit d'utiliser une classe mod??le personnalis??e
		// d??finie par PersonModel pour une meilleur flexibilit??

		List<Utilisateur> persons = personService.getAllPersons();
		List<PersonModel> modelPersons = new ArrayList<PersonModel>();
		// On copie les donn??es des personnes vers le mod??le
		for (int i = 0; i < persons.size(); i++) {
			PersonModel pm = new PersonModel();
			if (persons.get(i) instanceof Etudiant) {
				// permet de copier les donn??es d'un objet ?? l'autre ?? codition
				// d'avoir les meme attributs (getters/setters)
				BeanUtils.copyProperties((Etudiant) persons.get(i), pm);
				// On fixe le type (cet attribut ne sera pas copi?? automatiquement)
				pm.setTypePerson(PersonModel.TYPE_STUDENT);

				// Mettre la personne dans le mod??le
				modelPersons.add(pm);
			} else if (persons.get(i) instanceof Enseignant) {

				BeanUtils.copyProperties((Enseignant) persons.get(i), pm);
				pm.setTypePerson(PersonModel.TYPE_PROF);
				modelPersons.add(pm);
			} else if (persons.get(i) instanceof CadreAdministrateur) {
				BeanUtils.copyProperties((CadreAdministrateur) persons.get(i), pm);
				pm.setTypePerson(PersonModel.TYPE_CADRE_ADMIN);
				modelPersons.add(pm);
			}
		}

		// Mettre la liste des personnes dans le mod??le de Spring MVC
		model.addAttribute("personList", modelPersons);

		return "admin/form";
	}

	@RequestMapping(value = "addPerson", method = RequestMethod.POST)
	public String process(@Valid @ModelAttribute("personModel") PersonModel person, BindingResult bindingResult,
			Model model, HttpServletRequest rq) {

		// En cas d'erreur de validation
		if (bindingResult.hasErrors()) {
			// rq.setAttribute("typePerson", +person.getTypePerson());
			return "admin/form";
		}

		// Copier les donn??es de l'objet PersonModel vers l'objet Etudiant (cas du
		// formulaire de l'??tudiant)
		if (person.getTypePerson() == PersonModel.TYPE_STUDENT) {
			Etudiant etd = new Etudiant();
			BeanUtils.copyProperties(person, etd);

			personService.addPerson(etd);

		}
		// Copier les donn??es de l'objet PersonModel vers l'objet Enseignant (cas du
		// formulaire de l'Enseignant)

		else if (person.getTypePerson() == PersonModel.TYPE_PROF) {
			Enseignant prof = new Enseignant();
			BeanUtils.copyProperties(person, prof);
			personService.addPerson(prof);

		}
		// Copier les donn??es de l'objet PersonModel vers l'objet CadreAdministrateur
		// (cas du
		// formulaire de CadreAdministrateur)
		else if (person.getTypePerson() == PersonModel.TYPE_CADRE_ADMIN) {
			CadreAdministrateur ca = new CadreAdministrateur();
			BeanUtils.copyProperties(person, ca);
			personService.addPerson(ca);

		}

		// rediriger vers l'action shwoForm avec le meme type de personne
		return "redirect:/admin/showForm?typePerson=" + person.getTypePerson();
	}

	@RequestMapping(value = "updatePersonForm/{idPerson}", method = RequestMethod.GET)
	public String updatePersonForm(@PathVariable int idPerson, Model model) {

		// On reoit comme param??tre l'id de la personne ?? mettre ?? jour
		Utilisateur utl = personService.getPersonById(Long.valueOf(idPerson));

		// On construit le mod??le
		PersonModel pm = new PersonModel();

		// En fonction due type de l'utilisateur ?? modifier
		// Ceci va nous pemettre d'afficher un formulaire adapt??
		// slon le type de la personne
		if (utl instanceof Etudiant) {
			BeanUtils.copyProperties((Etudiant) utl, pm);
			pm.setTypePerson(PersonModel.TYPE_STUDENT);
		} else if (utl instanceof Enseignant) {
			BeanUtils.copyProperties((Enseignant) utl, pm);
			pm.setTypePerson(PersonModel.TYPE_PROF);
		} else if (utl instanceof CadreAdministrateur) {
			BeanUtils.copyProperties((CadreAdministrateur) utl, pm);
			pm.setTypePerson(PersonModel.TYPE_CADRE_ADMIN);
		}

		// Initialiser le modele avec la personne
		model.addAttribute("personModel", pm);

		return "admin/updateForm";
	}

	@RequestMapping(value = "serachPerson", method = RequestMethod.GET)
	public String serachPerson(@RequestParam String cin, Model model) {

		// On reoit comme param??tre l'id de la personne ?? mettre ?? jour
		Utilisateur utl = personService.getPersonByCin(cin);

		if (utl == null) {

			// Initialiser le modele avec la personne
			model.addAttribute("personModel", new ArrayList<PersonModel>());
		} else {

			// On construit le mod??le
			PersonModel pm = new PersonModel();

			// En fonction due type de l'utilisateur ?? modifier
			// Ceci va nous pemettre d'afficher un formulaire adapt??
			// slon le type de la personne
			if (utl instanceof Etudiant) {
				BeanUtils.copyProperties((Etudiant) utl, pm);
				pm.setTypePerson(PersonModel.TYPE_STUDENT);
			} else if (utl instanceof Enseignant) {
				BeanUtils.copyProperties((Enseignant) utl, pm);
				pm.setTypePerson(PersonModel.TYPE_PROF);
			} else if (utl instanceof CadreAdministrateur) {
				BeanUtils.copyProperties((CadreAdministrateur) utl, pm);
				pm.setTypePerson(PersonModel.TYPE_CADRE_ADMIN);

			}
			List<PersonModel> modelPersons = new ArrayList<PersonModel>();
			modelPersons.add(pm);
			// Initialiser le modele avec la personne
			model.addAttribute("personList", modelPersons);
		}
		return "admin/listPersons";
	}

	@RequestMapping("updatePerson")
	public String updatePerson(@Valid @ModelAttribute("personModel") PersonModel person, BindingResult bindingResult,
			Model model) {

		// En cas d'erreur
		if (bindingResult.hasErrors()) {

			return "admin/updateForm";
		}

		// On copie les donn??es du mod??le vers l'objet m??tier puis on appel le service
		// pour faire la mise ?? jour
		if (person.getTypePerson() == PersonModel.TYPE_STUDENT) {
			Etudiant etd = new Etudiant();
			BeanUtils.copyProperties(person, etd);

			personService.updatePerson(etd);

		} else if (person.getTypePerson() == PersonModel.TYPE_PROF) {
			Enseignant prof = new Enseignant();
			BeanUtils.copyProperties(person, prof);
			personService.updatePerson(prof);

		} else if (person.getTypePerson() == PersonModel.TYPE_CADRE_ADMIN) {
			CadreAdministrateur ca = new CadreAdministrateur();
			BeanUtils.copyProperties(person, ca);
			personService.updatePerson(ca);

		}

		// Mettre le message de succ??s dans le mod??le
		model.addAttribute("msg", "Op??ration effectu??e avec succ??s");

		return "admin/updateForm";
	}

	@RequestMapping("managePersons")
	public String managePersons(Model model) {

		List<Utilisateur> persons = personService.getAllPersons();
		List<PersonModel> modelPersons = new ArrayList<PersonModel>();

		// Copier les objets metier vers PersonModel plus flexible
		for (int i = 0; i < persons.size(); i++) {
			PersonModel pm = new PersonModel();
			if (persons.get(i) instanceof Etudiant) {
				BeanUtils.copyProperties((Etudiant) persons.get(i), pm);
				pm.setTypePerson(PersonModel.TYPE_STUDENT);
				modelPersons.add(pm);
			} else if (persons.get(i) instanceof Enseignant) {
				BeanUtils.copyProperties((Enseignant) persons.get(i), pm);
				pm.setTypePerson(PersonModel.TYPE_PROF);
				modelPersons.add(pm);
			} else if (persons.get(i) instanceof CadreAdministrateur) {
				BeanUtils.copyProperties((CadreAdministrateur) persons.get(i), pm);
				pm.setTypePerson(PersonModel.TYPE_CADRE_ADMIN);
				modelPersons.add(pm);
			}
		}

		model.addAttribute("personList", modelPersons);

		return "admin/listPersons";
	}

	@RequestMapping(value = "deletePerson/{idPerson}", method = RequestMethod.GET)
	public String delete(@PathVariable int idPerson) {

		personService.deletePerson(Long.valueOf(idPerson));

		return "redirect:/admin/managePersons";
	}
////methode pour l'importation
	@PostMapping("/ImportData")
	public String importToExcel(@RequestPart("file") MultipartFile file,HttpServletResponse response) throws IOException {
		System.out.println(file.getOriginalFilename());
        int d=ExcelHandler.importNbreColonne("C:\\Users\\HP\\Desktop\\data.xlsx", 0);
        System.out.println(d);
		if(Files.notExists(Paths.get("C:\\excel\\"))) {
				        Files.createDirectory(Paths.get("C:\\excel\\"));
			        }
		            if(Files.notExists(Paths.get("C:\\excel\\").resolve(file.getOriginalFilename()))){
		    	        Files.copy(file.getInputStream(), Paths.get("C:\\excel\\").resolve(file.getOriginalFilename()));
		 }
		            
		            Excel_Importer e = new Excel_Importer(file);
		            /// 1
		            if(e.checkTheFormat()==false) {
						System.out.println(e.checkTheFormat()+" doesn't have the desired extension");
						return "admin/ErrFile";
					
					}else {
						
						FileInputStream f = new FileInputStream(new File("C:\\excel\\"+file.getOriginalFilename()));
						Map<String, Map<String, Double>> malist = e.getCellValues();
						
						//liste d'inscription qu'on va le mettre dand bd
						List<InscriptionAnnuelle> listin = new ArrayList<InscriptionAnnuelle>();
						
						//liste de reinscription
						List<InscriptionAnnuelle> listReinscription = new ArrayList<InscriptionAnnuelle>();
						
						for(String s : malist.keySet()) {
							Map<String,Double> m = malist.get(s);
							
							String type = (String) m.keySet().toArray()[0];
							System.out.println("CNE : "+s+" TYPE : "+ type);
							
							List<Etudiant> listEt = e.getlistEtudiant();
							
							Etudiant et = null;
							
							
								Etudiant ete = etSer.getEtudiantByCne(s);
								if(ete ==null) {
									
									for(Etudiant etud : listEt) {
										System.out.println(etud.getCne());
										if(etud.getCne().equals(s)){
											System.out.println(s);
											et = etud;
											break;
										}
									}
									
									etSer.save(et);//db
									System.out.println(" doesn't existe in DB : " + et.getCne());
									InscriptionAnnuelle insAnn = new InscriptionAnnuelle();
									
									insAnn.setEtudiant(et);
									insAnn.setNiveau(nivSer.getNiveau(((Double) m.get(m.keySet().toArray()[0])).longValue()));
									insAnn.setAnnee(2022);
									
									listin.add(insAnn);
									
									//reinsc
								}else {
									et = ete;
									InscriptionAnnuelle i = inscSer.getInscription(nivSer.getNiveau(((Double) m.get(m.keySet().toArray()[0])).longValue()-1), et);
									
									if(i==null){
										System.out.println("----------errooooor --------n'est pas inscri");
									}else {
			
										InscriptionAnnuelle reins = new InscriptionAnnuelle();
										
										reins.setAnnee(2022);
										reins.setEtudiant(et);
										reins.setNiveau(nivSer.getNiveau(((Double) m.get(m.keySet().toArray()[0])).longValue()));
										
										
										if(i.getValidation().equals("v")) {
											listReinscription.add(reins);
										}else {
											System.err.println("----------- erroooor ------ vous avez pas valider");
										}
									}
									System.out.println(" existe in DB : " + et.getCne());
								}
							}
							
							System.out.println("----- in" + listin);
							
							System.out.println("-------- rein " + listReinscription);
							
							inscSer.setAll(listin);
							inscSer.setAll(listReinscription);
							
					}
						return "admin/TrueFile";
				      
						}

	
	
	@GetMapping("exportPersons")
	public void exportToExcel(HttpServletResponse response) throws IOException {
		response.setContentType("application/octet-stream");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=users_" + currentDateTime + ".xlsx";
		response.setHeader(headerKey, headerValue);

		List<Utilisateur> persons = personService.getAllPersons();

		ExcelExporter excelExporter = personService.preparePersonExport(persons);

		excelExporter.export(response);
	}
}
