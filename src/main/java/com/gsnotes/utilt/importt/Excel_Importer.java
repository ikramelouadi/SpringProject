package com.gsnotes.utilt.importt;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;
import org.yaml.snakeyaml.composer.ComposerException;

import com.gsnotes.bo.Etudiant;

import javassist.bytecode.Descriptor.Iterator;

public class Excel_Importer {
	
	private MultipartFile file;
	private XSSFWorkbook workbook;
	private XSSFSheet sheet;

	private Map<String,Map<String,Double>> malist = new HashMap<>();
	
	private List<Etudiant> listEt = new ArrayList<Etudiant>();
	
	public Excel_Importer(MultipartFile file) throws IllegalStateException, IOException{
			this.file=file;
		
		}
	
	
	public Map<String, Map<String, Double>> getCellValues() throws IOException {
		
				FileInputStream excelFile = new FileInputStream(new File("C:\\excel\\"+file.getOriginalFilename()));
				
				workbook = new XSSFWorkbook(excelFile);
				
				sheet = workbook.getSheetAt(1);
				
				System.out.println("nbr lines "+sheet.getLastRowNum());
				
				int fRow = sheet.getFirstRowNum();//num de lign 1
				int lRow = sheet.getLastRowNum();//num lign dernier
				
				List<String> cne = new ArrayList<String>();
				List<String> type = new ArrayList<String>();
				List<Double> niveau = new ArrayList<>();
				
				for(int i = fRow;i<=lRow;i++) {
					
					Row row = sheet.getRow(i);
				
					if(i>=1){
						Etudiant e = new Etudiant();

						for(int c = row.getFirstCellNum(); c<row.getLastCellNum();c++) {
							Cell cell = row.getCell(c,Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
							if(c==1) {
								e.setCne(cell.getStringCellValue().trim());
								cne.add(cell.getStringCellValue().trim());
							//System.out.println(cell.getStringCellValue().trim());
							
							}else if(c==4) {
								
								niveau.add((Double)cell.getNumericCellValue());
								//System.out.println((Double)cell.getNumericCellValue()+"-------");
							}else if(c==5) {
								
								type.add(cell.getStringCellValue());
							}
							
							if(c==2) {
								e.setNom(cell.getStringCellValue());
//								System.out.println(cell.getStringCellValue());
							}
							
							if(c==3) {
								e.setPrenom(cell.getStringCellValue());
								//e.setCin("n12335");
								//System.out.println(cell.getStringCellValue());
							}
								
							
						}
						
						listEt.add(e);
						
					}
				}
				
				for(Etudiant e : listEt) {
					System.out.println("Etudiant : "+e.getCne() + " , ");
				}
		

				int k=0;
				for(String n : type) {
					Map<String,Double> m1 = new HashMap<String, Double>();
					m1.put(n,niveau.get(k));
					malist.put(cne.get(k), m1);
					k++;
				}

				
				System.out.println("------" + malist);
                //System.out.println(malist.size());
				if (workbook != null) {
					workbook.close();
				}
			
				return malist;	
	}


	public Boolean checkTheFormat(){
			
			String[] extensions = {"xlsx","xlsm","xlsb","xltx","xltm","xls","xlt","xls","xml","xlam","xla","xlw","xlr"};
			Boolean endsWith = false;
			String fN = file.getOriginalFilename();
		    String extension = fN.substring(fN.lastIndexOf(".") + 1,fN.length());

			for(String e : extensions) {
			    System.out.println(extension.equals(e));
				if(e.equals(extension)) {
					endsWith = true;
					break;
				}
			}
			return endsWith;
	}
	
	
	public List<Etudiant> getlistEtudiant(){
		
		return listEt;
	}

}



