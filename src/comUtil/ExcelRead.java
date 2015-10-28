package comUtil;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelRead {
	private String fileDir = "";
	public ExcelRead(String fileName) {
		// TODO Auto-generated constructor stub
		this.fileDir = fileName;
	}

	// �ж�excel�汾
	static Workbook openWorkbook(InputStream in, String filename) throws IOException {
		String extFileName = filename.substring(filename.lastIndexOf("."));
		Workbook wb = null;
		if (extFileName.endsWith(".xlsx")) {
			wb = new XSSFWorkbook(in);// Excel 2007
		} else {
			wb = (Workbook) new HSSFWorkbook(in);// Excel 2003
		}
		return wb;
	}

	public ArrayList<String[]> getExcelData() throws Exception {
		InputStream in = new FileInputStream(fileDir); // ����������
		Workbook wb = openWorkbook(in, fileDir);// ��ȡExcel�ļ�����
		Sheet sheet = wb.getSheetAt(0);// ��ȡ�ļ���ָ��������m Ĭ�ϵĵ�һ��
		ArrayList<String[]> list = new ArrayList<String[]>();
		Row row = null;
		Cell cell = null;
		int totalRows = sheet.getPhysicalNumberOfRows(); // ������
		int totalCells = sheet.getRow(0).getPhysicalNumberOfCells();// ������
		for (int i = 0; i < totalRows; i++) {
			// ����һ������ �����洢ÿһ�е�ֵ
			String[] str = new String[totalCells];
			row = sheet.getRow(i);
			for (int j = 0; j < totalCells; j++) {
				cell = (Cell) sheet.getCellComment(j, i);
				cell = row.getCell(j);
//				System.out.println(j + "DDDDDDDDDD");
				 str[j] = cell==null?"":cell.toString();
			}
		}
		for (int r = 0; r < totalRows; r++) {
			row = sheet.getRow(r);
			// ����һ������ �����洢ÿһ�е�ֵ
			String[] str = new String[totalCells];
			System.out.print("��" + r + "��");
			for (int c = 0; c < totalCells; c++) {
				cell = row.getCell(c);
				String cellValue = "";
				if (null != cell) {
					// �������ж����ݵ�����
					switch (cell.getCellType()) {
					case HSSFCell.CELL_TYPE_NUMERIC: // ����
						cellValue = cell.getNumericCellValue() + "";
						// ʱ���ʽ
						if (HSSFDateUtil.isCellDateFormatted(cell)) {
							Date dd = cell.getDateCellValue();
							DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
							cellValue = df.format(dd);
						}
						break;
					case HSSFCell.CELL_TYPE_STRING: // �ַ���
						cellValue = cell.getStringCellValue();
						break;
					case HSSFCell.CELL_TYPE_BOOLEAN: // Boolean
						cellValue = cell.getBooleanCellValue() + "";
						break;
					case HSSFCell.CELL_TYPE_FORMULA: // ��ʽ
						cellValue = cell.getCellFormula() + "";
						break;
					case HSSFCell.CELL_TYPE_BLANK: // ��ֵ
						cellValue = "";
						break;
					case HSSFCell.CELL_TYPE_ERROR: // ����
						cellValue = "�Ƿ��ַ�";
						break;
					default:
						cellValue = "δ֪����";
						break;
					}
					System.out.print("   " + cellValue + "\t");
					str[c] = cellValue;
				}else{
					str[c] = "";
				}
			}
			System.out.println();
			// �Ѹջ�ȡ���д���list
			list.add(str);
		}
		// ����ֵ����
		return list;
	}

	public static void main(String[] args) throws Exception {
		String fileName = "d:/PO.xlsx";
		ExcelRead upload = new ExcelRead(fileName);
		List<?> list = upload.getExcelData();
		System.out.println(list.size());
		for(int i=0;i<list.size();i++){
			String[] row = (String[]) list.get(i);
			System.out.println(row.toString());
		}
	}
}
