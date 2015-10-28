package comUtil;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author samzheng
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.TableModel;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

import dmdata.DataManager;

public class JTableExportExcel {

    JTable table;
    FileOutputStream fos;
    JFileChooser jfc = new JFileChooser();
    
    public JTableExportExcel(){
        jfc.addChoosableFileFilter(new FileFilter() {

            public boolean accept(File file) {
                return (file.getName().indexOf("xls") != -1);
            }

            public String getDescription() {
                return "Excel";
            }
        });
        jfc.showSaveDialog(null);
        File file = jfc.getSelectedFile();
        try {
            this.fos = new FileOutputStream(file + ".xls");
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    public JTableExportExcel(JTable table) {
        this.table = table;
        jfc.addChoosableFileFilter(new FileFilter() {

            public boolean accept(File file) {
                return (file.getName().indexOf("xls") != -1);
            }

            public String getDescription() {
                return "Excel";
            }
        });
        TableModel tm = table.getModel();
        int row = tm.getRowCount();
        int cloumn = tm.getColumnCount();
        if (row < 1) {
            JOptionPane.showMessageDialog(table, new String("无明细数据!"));
            return;
        }
        jfc.showSaveDialog(null);
        File file = jfc.getSelectedFile();
        try {
            this.fos = new FileOutputStream(file + ".xls");
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    public void export() {
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet hs = wb.createSheet();
        TableModel tm = table.getModel();
        int row = tm.getRowCount();
        int cloumn = tm.getColumnCount();
        if (row < 1) {
            JOptionPane.showMessageDialog(table, new String("无数据输出!"));
            return;
        }
        HSSFCellStyle style = wb.createCellStyle();
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setFillForegroundColor(HSSFColor.LIGHT_GREEN.index);
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        HSSFFont font = wb.createFont();
        font.setFontHeightInPoints((short) 11);
        style.setFont(font);
        HSSFCellStyle style1 = wb.createCellStyle();
        style1.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style1.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style1.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style1.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style1.setFillForegroundColor(HSSFColor.ORANGE.index);
        style1.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        HSSFFont font1 = wb.createFont();
        font1.setFontHeightInPoints((short) 15);
        font1.setBoldweight((short) 700);
        style1.setFont(font);
        for (int i = 0; i < row + 1; i++) {
            HSSFRow hr = hs.createRow(i);
            for (int j = 0; j < cloumn; j++) {
                if (i == 0) {
                    String value = tm.getColumnName(j);
                    int len = value.length();
                    hs.setColumnWidth((short) j, (short) (len * 400));
                    HSSFRichTextString srts = new HSSFRichTextString(value);
                    HSSFCell hc = hr.createCell((short) j);
                    //hc.setEncoding((short) 1);
                    hc.setCellStyle(style1);
                    hc.setCellValue(srts);
                } else {
                    String value = "";
                    if(tm.getValueAt(i - 1, j) == null) {
                        value = "";
                    } else {
                        value = tm.getValueAt(i - 1, j).toString();
                    }
                    HSSFRichTextString srts = new HSSFRichTextString(value);
                    HSSFCell hc = hr.createCell((short) j);
                    //hc.setEncoding((short) 1);
                    hc.setCellStyle(style);
                    if (value.equals("") || value == null) {
                        hc.setCellValue(new HSSFRichTextString(""));
                    } else {
                        hc.setCellValue(srts);
                    }
                }
            }
        }
        try {
            wb.write(fos);
            fos.close();
            JOptionPane.showMessageDialog(this.jfc, new String("数据输出成功!"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public void exportExcelFromDataManagerByCols(DataManager dm) {
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet hs = wb.createSheet();
        int row = dm.getCurrentCount();
        int cloumn = dm.getColCount();
        if (row < 0) {
            JOptionPane.showMessageDialog(table, new String("无数据输出!"));
            return;
        }
        HSSFCellStyle style = wb.createCellStyle();
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setFillForegroundColor(HSSFColor.LIGHT_GREEN.index);
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        HSSFFont font = wb.createFont();
        font.setFontHeightInPoints((short) 11);
        style.setFont(font);
        HSSFCellStyle style1 = wb.createCellStyle();
        style1.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style1.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style1.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style1.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style1.setFillForegroundColor(HSSFColor.ORANGE.index);
        style1.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        HSSFFont font1 = wb.createFont();
        font1.setFontHeightInPoints((short) 15);
        font1.setBoldweight((short) 700);
        style1.setFont(font);
        for (int i = 0; i <= row; i++) {
            HSSFRow hr = hs.createRow(i);
            for (int j = 0; j < cloumn; j++) {
                if (i == 0) {
                    String value = dm.getCol(j);
                    int len = value.length();
                    hs.setColumnWidth((short) j, (short) (len * 400));
                    HSSFRichTextString srts = new HSSFRichTextString(value);
                    HSSFCell hc = hr.createCell((short) j);
                    //hc.setEncoding((short) 1);
                    hc.setCellStyle(style1);
                    hc.setCellValue(srts);
                } else {
                    String value = "";
                    if(dm.getString(j, i-1) == null) {
                        value = "";
                    } else {
                        value = dm.getString(j, i-1);
                    }
                    HSSFRichTextString srts = new HSSFRichTextString(value);
                    HSSFCell hc = hr.createCell((short) j);
                    //hc.setEncoding((short) 1);
                    hc.setCellStyle(style);
                    if (value.equals("") || value == null) {
                        hc.setCellValue(new HSSFRichTextString(""));
                    } else {
                        hc.setCellValue(srts);
                    }
                }
            }
        }
        try {
            wb.write(fos);
            fos.close();
            JOptionPane.showMessageDialog(this.jfc, new String("数据输出成功!"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
//方法的調用
//JTableExportExcel exportExcel = new JTableExportExcel(this.jTable1); 
//exportExcel.export();
