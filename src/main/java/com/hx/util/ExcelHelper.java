package com.hx.util;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * excel导出工具类
 * @author Oven
 *
 */
public class ExcelHelper {

	/**
	 * 复制一个单元格样式到目的单元格样式
	 * @param fromStyle
	 * @param toStyle
	 */
	/*public static void copyCellStyle(CellStyle fromStyle,
									 CellStyle toStyle) {
		//边框和边框颜色
		toStyle.setTopBorderColor(fromStyle.getTopBorderColor());
		toStyle.setBottomBorderColor(fromStyle.getBottomBorderColor());
		toStyle.setRightBorderColor(fromStyle.getRightBorderColor());
		toStyle.setLeftBorderColor(fromStyle.getLeftBorderColor());

		//背景和前景
		toStyle.setFillBackgroundColor(fromStyle.getFillBackgroundColor());
		toStyle.setFillForegroundColor(fromStyle.getFillForegroundColor());

		toStyle.setDataFormat(fromStyle.getDataFormat());
		toStyle.setHidden(fromStyle.getHidden());
		toStyle.setIndention(fromStyle.getIndention());//首行缩进
		toStyle.setLocked(fromStyle.getLocked());
		toStyle.setRotation(fromStyle.getRotation());//旋转
		toStyle.setWrapText(fromStyle.getWrapText());

	}*/

	/**
	 * 复制单元格
	 *
	 * @param srcCell
	 * @param distCell
	 * @param copyValueFlag
	 *            true则连同cell的内容一起复制
	 */
	/*public static void copyCell(Workbook wb,Cell srcCell, Cell distCell,
								boolean copyValueFlag) {
		//样式
		distCell.setCellStyle(srcCell.getCellStyle());
		//评论
		if (srcCell.getCellComment() != null) {
			distCell.setCellComment(srcCell.getCellComment());
		}
		// 不同数据类型处理
		int srcCellType = srcCell.getCellType();
		distCell.setCellType(srcCellType);
		if (copyValueFlag) {
			if (srcCellType == Cell.CELL_TYPE_NUMERIC) {
				if (DateUtil.isCellDateFormatted(srcCell)) {
					distCell.setCellValue(srcCell.getDateCellValue());
				} else {
					distCell.setCellValue(srcCell.getNumericCellValue());
				}
			} else if (srcCellType == Cell.CELL_TYPE_STRING) {
				distCell.setCellValue(srcCell.getRichStringCellValue());
			} else if (srcCellType == Cell.CELL_TYPE_BLANK) {
				// nothing21
			} else if (srcCellType == Cell.CELL_TYPE_BOOLEAN) {
				distCell.setCellValue(srcCell.getBooleanCellValue());
			} else if (srcCellType == Cell.CELL_TYPE_ERROR) {
				distCell.setCellErrorValue(srcCell.getErrorCellValue());
			} else if (srcCellType == Cell.CELL_TYPE_FORMULA) {
				distCell.setCellFormula(srcCell.getCellFormula());
			} else { // nothing29
			}
		}
	}*/

	/**
	 * 行复制功能
	 * @param fromRow
	 * @param toRow
	 */
	/*public static void copyRow(Workbook wb,Row fromRow,Row toRow,boolean copyValueFlag){

		toRow.setHeight(fromRow.getHeight());
		for (Iterator<Cell> cellIt = fromRow.cellIterator(); cellIt.hasNext();) {
			Cell tmpCell = cellIt.next();
			Cell newCell = toRow.createCell(tmpCell.getColumnIndex());
			copyCell(wb,tmpCell, newCell, copyValueFlag);
		}
	}*/

	/**
	 * 导出excel
	 * @param headers 表格头部
	 * @param bodyContent 表格内容，数组与头部需自行一一对应
	 * @param fileName 文件名，不带后缀
	 * @param fileType 文件类型，即文件后缀
	 * @param response 请求回应
	 */
	public static void exportExcel(List<String> headers, List<Object[]> bodyContent, String fileName, String fileType, HttpServletResponse response){
		try {
			Workbook workbook;
			if ("xlsx".equals(fileType)) {
				workbook = new XSSFWorkbook();
			} else {
				workbook = new HSSFWorkbook();
			}
			// 产生工作表对象
			Sheet sheet = workbook.createSheet();

			// sheet表、单元格样式、数据集合
			createExcelHeader(workbook, sheet, headers);

			// sheet表、body的数据集合
			createExcelBody(workbook, sheet, bodyContent);

			OutputStream out = response.getOutputStream();// 取得输出流
			response.reset();// 清空输出流
			response.setCharacterEncoding("UTF-8");

			// 设置返回的头字段:http协议inline采用浏览器方式打开、attachment采用本地EXCEL方式打开
			response.setHeader("Content-disposition", "inline;filename = "+ java.net.URLEncoder.encode(fileName, "UTF-8") + "." + fileType);
			response.setContentType("application/vnd.ms-excel;charset=UTF-8");// 定义输出类型
			workbook.write(out);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 创建excel表头
	 * @param workbook
	 * @param sheet
	 * @param lists
	 */
	private static void createExcelHeader(Workbook workbook, Sheet sheet,
										  List<String> lists) {
		// 设置第一个工作表的名称为firstSheet
		workbook.setSheetName(0, "sheet");
		sheet.setDefaultColumnWidth(25);// 设置默认每一列的宽度
		CellStyle cellStyle = workbook.createCellStyle();
		Font font = workbook.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 设置粗体
		cellStyle.setFont(font);
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		Row row = sheet.createRow(0);
		Cell cell = null;
		for (int i = 0; i < lists.size(); i++) {
			cell = row.createCell(i);
			cell.setCellValue(lists.get(i));
			cell.setCellStyle(cellStyle);
		}
	}

	/**
	 * 创建excel表格数据
	 * @param workbook
	 * @param sheet
	 * @param list
	 */
	private static void createExcelBody(Workbook workbook, Sheet sheet,
										List<Object[]> list) {
		// 定义单元格样式
		CellStyle cellStyle = workbook.createCellStyle();
		CellStyle cs = workbook.createCellStyle();
		cs.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 对齐方式

		DataFormat dataFormat = workbook.createDataFormat();
		cellStyle.setDataFormat(dataFormat.getFormat("yyyy-mm-dd hh:mm"));// 时间格式

		Row row;
		Cell cell;
		if(list.size()>0){
			for(int i=0;i<list.size();i++){
				Object[] arr = list.get(i);

				row = sheet.createRow(i + 1);

				int index = 0;
				for (Object o : arr) {
					cell = row.createCell(index++ );
					cell.setCellType(Cell.CELL_TYPE_STRING);
					cell.setCellStyle(cs);
					cell.setCellValue(o == null ? "" : o.toString());
				}
			}
		}
	}

	/**
	 * 读取excel文件
	 * @return 单元格字符数组
	 */
	public static ArrayList<ArrayList<String>> readExcel(InputStream is, String fileType) {
		ArrayList<ArrayList<String>> Row = new ArrayList<ArrayList<String>>();
		try {
			Workbook workBook;
			if ("xlsx".equals(fileType)) {
				workBook = new XSSFWorkbook(is);
			} else {
				workBook = new HSSFWorkbook(is);
			}

			for (int numSheet = 0; numSheet < workBook.getNumberOfSheets(); numSheet++) {
				Sheet sheet = workBook.getSheetAt(numSheet);
				if (sheet == null) {
					continue;
				}
				// 循环行Row
				for (int rowNum = 0; rowNum <= sheet.getLastRowNum(); rowNum++) {
					Row row = sheet.getRow(rowNum);
					if (row == null) {
						continue;
					}

					// 循环列Cell
					ArrayList<String> arrCell = new ArrayList<String>();
					for (int cellNum = 0; cellNum <= row.getLastCellNum(); cellNum++) {
						Cell cell = row.getCell(cellNum);
						if (cell == null) {
							continue;
						}
						arrCell.add(getValue(cell));
					}
					Row.add(arrCell);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return Row;
	}

	/**
	 * 读取excel文件
	 * @return 单元格字符数组
	 */
	/*public static ArrayList<ArrayList<String>> readExcel(InputStream is, String fileType, int sheetNum) {
		ArrayList<ArrayList<String>> rowData = new ArrayList<ArrayList<String>>();
		try {
			Workbook workBook;
			if ("xlsx".equals(fileType)) {
				workBook = new XSSFWorkbook(is);
			} else {
				workBook = new HSSFWorkbook(is);
			}

			Sheet sheet = workBook.getSheetAt(sheetNum);
			if (sheet == null) {
				return null;
			}
			// 循环行Row
			for (int rowNum = 0; rowNum <= sheet.getLastRowNum(); rowNum++) {
				Row row = sheet.getRow(rowNum);
				if (row == null) {
					continue;
				}

				// 循环列Cell
				ArrayList<String> arrCell = new ArrayList<String>();
				for (int cellNum = 0; cellNum <= row.getLastCellNum(); cellNum++) {
					Cell cell = row.getCell(cellNum);
					if (cell == null) {
						continue;
					}
					arrCell.add(getValue(cell));
				}
				rowData.add(arrCell);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return rowData;
	}
*/
	/**
	 * 读取excel文件
	 *
	 * @param
	 *
	 * @return
	 */
	/*public static ArrayList<ArrayList<String>> readExcel(String path) {
		ArrayList<ArrayList<String>> Row = new ArrayList<ArrayList<String>>();
		try {
			Workbook workBook = null;
			try {
				workBook = new XSSFWorkbook(path);
			} catch (Exception ex) {
				workBook = new HSSFWorkbook(new FileInputStream(path));
			}

			for (int numSheet = 0; numSheet < workBook.getNumberOfSheets(); numSheet++) {
				Sheet sheet = workBook.getSheetAt(numSheet);
				if (sheet == null) {
					continue;
				}
				// 循环行Row
				for (int rowNum = 0; rowNum <= sheet.getLastRowNum(); rowNum++) {
					Row row = sheet.getRow(rowNum);
					if (row == null) {
						continue;
					}

					// 循环列Cell
					ArrayList<String> arrCell = new ArrayList<String>();
					for (int cellNum = 0; cellNum <= row.getLastCellNum(); cellNum++) {
						Cell cell = row.getCell(cellNum);
						if (cell == null) {
							continue;
						}
						arrCell.add(getValue(cell));
					}
					Row.add(arrCell);
				}
			}
		} catch (IOException e) {
			//System.out.println("e:" + e);
			return null;
		}
		return Row;
	}*/

	// 读取excel文件辅助
	private static String getValue(Cell cell) {
		String value = null;
		switch (cell.getCellType()) {
			case HSSFCell.CELL_TYPE_STRING:
				value = cell.getStringCellValue();
				break;
			case HSSFCell.CELL_TYPE_NUMERIC:
				if (HSSFDateUtil.isCellDateFormatted(cell)) {
					Date date = cell.getDateCellValue();
					if (date != null) {
						value = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
					} else {
						value = "";
					}
				} else {
					value = new DecimalFormat("0.00").format(cell.getNumericCellValue());
				}
				break;
			case HSSFCell.CELL_TYPE_FORMULA:
				// 导入时如果为公式生成的数据则无值
				if (!cell.getStringCellValue().equals("")) {
					value = cell.getStringCellValue();
				} else {
					value = cell.getNumericCellValue() + "";
				}
				break;
			case HSSFCell.CELL_TYPE_BLANK:
				value = "";
				break;
			case HSSFCell.CELL_TYPE_ERROR:
				value = "";
				break;
			case HSSFCell.CELL_TYPE_BOOLEAN:
				value = (cell.getBooleanCellValue() == true ? "true" : "false");
				break;
			default:
				value = "";
		}
		return value;

	}
}
