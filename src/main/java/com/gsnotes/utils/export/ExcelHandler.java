package com.gsnotes.utils.export;

import org.apache.poi.ss.usermodel.Cell;


import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;





import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class ExcelHandler {
   
	private static final ExcelHandler instance = new ExcelHandler();
	

	public ExcelHandler() {
		
	}

	
	public static final ExcelHandler getInstance() {
		return instance;
	}


	
	public static int importNbreColonne(String pFileName, int pSheet)  {

		//List<String> cne=new ArrayList<>();
		int colonne=0;
      
		try {
			Workbook workbook = null;
			try {
				FileInputStream excelFile = new FileInputStream(new File(pFileName));
				workbook = new XSSFWorkbook( excelFile);
				Sheet datatypeSheet = workbook.getSheetAt(pSheet);
				int rowNumber=0;
                                 
				Iterator<Row> iterator = datatypeSheet.iterator();

				while (iterator.hasNext()) {
					
                    Row currentRow = iterator.next();
					
					Iterator<Cell> cellIterator = currentRow.iterator();
					
                    int cid=0;
                   
					while (cellIterator.hasNext()) {
                      
						Cell currentCell = cellIterator.next();
									
                                                cid++;
                        
                        }
				
                       	
				colonne=cid;
				
			rowNumber++;
			return colonne;
				}
			} finally {
				if (workbook != null) {
					workbook.close();
				}
			}
		} catch (Exception ex) {
			
		}
		
		return colonne;
        
		
    
	}
}
