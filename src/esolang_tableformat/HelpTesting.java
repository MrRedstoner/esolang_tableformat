package esolang_tableformat;

import java.io.File;
import java.io.FileInputStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class HelpTesting {

	public static void main(String[] args) throws Exception {
		FileInputStream excelFile = new FileInputStream(new File("C:\\Users\\patog\\WorkFolders\\Eclipse\\GitWorkspace\\esolang_tableformat\\TableFormat_ConversionDefinition.xlsx"));
		XSSFWorkbook workbook = new XSSFWorkbook(excelFile);
        CellFormatToNumberConvertor.init(workbook);
        Sheet datatypeSheet = workbook.getSheetAt(0);
        //Iterator<Row> iterator = datatypeSheet.iterator();
        for(int i=25;i<49;i++){
        	Cell c=datatypeSheet.getRow(i).getCell(1);
        	System.out.println(datatypeSheet.getRow(i).getCell(0).getStringCellValue());
        	System.out.println(CellFormatToNumberConvertor.getNumberFor(c));
        }
        workbook.close();
	}
}