package util;
import java.io.File;
import java.util.Date;
import java.util.Vector;
import java.awt.Color;
import org.apache.poi.hssf.usermodel.*;
import java.io.FileOutputStream;
import org.apache.poi.hssf.util.Region;

public class textExcel {
  public textExcel() {
  }
  public static void main(String[] args) {
    textExcel textExcel1 = new textExcel();
    textExcel1.make();
  }
  public void make(){
    try{
    HSSFWorkbook wb = new HSSFWorkbook();
    HSSFSheet sheet = wb.createSheet("new sheet");

    HSSFRow row = sheet.createRow((short) 0);
    HSSFCell cell = row.createCell((short) 3);
    cell.setCellValue("This is a test of merging");

    sheet.addMergedRegion(new Region(0,(short)3,0,(short)10));

    // Write the output to a file
    FileOutputStream fileOut = new FileOutputStream("F:\\workbook.xls");
    wb.write(fileOut);
    fileOut.close();
    }catch(Exception e){
      e.printStackTrace();
    }

  }
}
