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

	// 判断excel版本
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
		InputStream in = new FileInputStream(fileDir); // 创建输入流
		Workbook wb = openWorkbook(in, fileDir);// 获取Excel文件对象
		Sheet sheet = wb.getSheetAt(0);// 获取文件的指定工作表m 默认的第一个
		ArrayList<String[]> list = new ArrayList<String[]>();
		Row row = null;
		Cell cell = null;
		int totalRows = sheet.getPhysicalNumberOfRows(); // 总行数
		int totalCells = sheet.getRow(0).getPhysicalNumberOfCells();// 总列数
		for (int i = 0; i < totalRows; i++) {
			// 创建一个数组 用来存储每一列的值
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
			// 创建一个数组 用来存储每一列的值
			String[] str = new String[totalCells];
			System.out.print("第" + r + "行");
			for (int c = 0; c < totalCells; c++) {
				cell = row.getCell(c);
				String cellValue = "";
				if (null != cell) {
					// 以下是判断数据的类型
					switch (cell.getCellType()) {
					case HSSFCell.CELL_TYPE_NUMERIC: // 数字
						cellValue = cell.getNumericCellValue() + "";
						// 时间格式
						if (HSSFDateUtil.isCellDateFormatted(cell)) {
							Date dd = cell.getDateCellValue();
							DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
							cellValue = df.format(dd);
						}
						break;
					case HSSFCell.CELL_TYPE_STRING: // 字符串
						cellValue = cell.getStringCellValue();
						break;
					case HSSFCell.CELL_TYPE_BOOLEAN: // Boolean
						cellValue = cell.getBooleanCellValue() + "";
						break;
					case HSSFCell.CELL_TYPE_FORMULA: // 公式
						cellValue = cell.getCellFormula() + "";
						break;
					case HSSFCell.CELL_TYPE_BLANK: // 空值
						cellValue = "";
						break;
					case HSSFCell.CELL_TYPE_ERROR: // 故障
						cellValue = "非法字符";
						break;
					default:
						cellValue = "未知类型";
						break;
					}
					System.out.print("   " + cellValue + "\t");
					str[c] = cellValue;
				}else{
					str[c] = "";
				}
			}
			System.out.println();
			// 把刚获取的列存入list
			list.add(str);
		}
		// 返回值集合
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
