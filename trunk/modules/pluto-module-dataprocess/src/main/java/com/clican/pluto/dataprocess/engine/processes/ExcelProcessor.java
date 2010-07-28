/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.dataprocess.engine.processes;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.clican.pluto.common.resource.AutoDecisionResource;
import com.clican.pluto.common.util.PropertyUtilS;
import com.clican.pluto.dataprocess.bean.ExcelExecBean;
import com.clican.pluto.dataprocess.engine.ProcessorContext;
import com.clican.pluto.dataprocess.engine.impl.BaseDataProcessor;
import com.clican.pluto.dataprocess.exception.DataProcessException;

/**
 * Excel读写执行的Processor
 * 
 * @author wei.zhang
 * 
 */
public class ExcelProcessor extends BaseDataProcessor {

	private List<ExcelExecBean> excelExecBeanList;

	public void setExcelExecBeanList(List<ExcelExecBean> excelExecBeanList) {
		this.excelExecBeanList = excelExecBeanList;
	}

	@SuppressWarnings("unchecked")
	public void writeExcel(ProcessorContext context, ExcelExecBean execBean)
			throws Exception {
		Workbook book = null;
		InputStream is = null;
		try {
			is = execBean.getInputStream();
		} catch (FileNotFoundException e) {

		}
		if (is != null) {
			book = WorkbookFactory.create(is);
			is.close();
		} else {
			book = new HSSFWorkbook();
		}
		CreationHelper createHelper = book.getCreationHelper();
		CellStyle dateStyle = book.createCellStyle();
		dateStyle.setDataFormat(createHelper.createDataFormat().getFormat(
				"yyyy-MM-dd"));

		CellStyle numStyle = book.createCellStyle();
		numStyle.setDataFormat(createHelper.createDataFormat().getFormat(
				"0.00000000"));

		CellStyle intNumStyle = book.createCellStyle();
		intNumStyle.setDataFormat(createHelper.createDataFormat()
				.getFormat("0"));

		List<Object> result = context.getAttribute(execBean.getParamName());
		String[] columns = execBean.getColumns();
		if (execBean.getColumns() != null) {
			columns = execBean.getColumns();
		} else {
			columns = ((List<String>) context.getAttribute(execBean
					.getColumnsVarName())).toArray(new String[] {});
		}
		String sheetName;
		if (StringUtils.isNotEmpty(execBean.getSheetName())) {
			sheetName = execBean.getSheetName();
		} else {
			sheetName = context.getAttribute(execBean.getSheetVarName())
					.toString();
		}
		// int number = book.getNumberOfSheets();
		Sheet sheet = book.createSheet(sheetName);
		int rowNum = 0;
		Row firstRow = sheet.createRow(rowNum++);
		for (int i = 0; i < columns.length; i++) {
			Cell cell = firstRow.createCell(i);
			cell.setCellType(Cell.CELL_TYPE_STRING);
			cell.setCellValue(columns[i]);
		}

		for (int i = 0; i < result.size(); i++) {
			Object row = result.get(i);
			Row dataRow = sheet.createRow(rowNum++);

			for (int j = 0; j < columns.length; j++) {
				Object obj = PropertyUtilS.getNestedProperty(row, columns[j]);
				Cell cell = dataRow.createCell(j);
				if (obj == null) {
					cell.setCellType(Cell.CELL_TYPE_BLANK);
				} else {
					if (obj instanceof String) {
						cell.setCellType(Cell.CELL_TYPE_STRING);
						cell.setCellValue(obj.toString());
					} else if (obj instanceof Date) {
						cell.setCellValue((Date) obj);
						cell.setCellStyle(dateStyle);
					} else if (obj instanceof Integer || obj instanceof Long) {
						cell.setCellType(Cell.CELL_TYPE_NUMERIC);
						cell.setCellStyle(intNumStyle);
						cell.setCellValue(new Double(obj.toString()));
					} else if (obj instanceof Number) {
						cell.setCellType(Cell.CELL_TYPE_NUMERIC);
						cell.setCellStyle(numStyle);
						cell.setCellValue(new Double(obj.toString()));
					} else {
						throw new DataProcessException("不支持的Excel格式类型");
					}
				}
			}
		}

		OutputStream os = null;
		try {
			os = execBean.getOutputStream();
			book.write(os);
		} finally {
			if (os != null) {
				os.close();
			}
		}
	}

	public void readExcel(ProcessorContext context, ExcelExecBean execBean)
			throws Exception {
		InputStream is = new AutoDecisionResource(execBean.getResource())
				.getInputStream();
		try {
			Workbook book = WorkbookFactory.create(is);
			Sheet sheet = book.getSheet(execBean.getSheetName());
			List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
			List<String> names = new ArrayList<String>();
			Map<String, String> typeMap = execBean.getTypeMap();
			int firstRow = sheet.getFirstRowNum(), lastRow = sheet
					.getLastRowNum();
			for (int rowIdx = firstRow; rowIdx < lastRow; rowIdx++) {
				Row excelRow = sheet.getRow(rowIdx);

				short minColIx = excelRow.getFirstCellNum();
				short maxColIx = excelRow.getLastCellNum();

				Map<String, Object> row = new HashMap<String, Object>();

				for (int colIdx = minColIx; colIdx < maxColIx; colIdx++) {
					Cell cell = excelRow.getCell(colIdx,
							Row.CREATE_NULL_AS_BLANK);

					if (rowIdx == 0) {
						names.add(cell.getStringCellValue());
					} else {
						String type = null;
						if (names.size() > colIdx) {
							type = typeMap.get(names.get(colIdx));
						}
						if (StringUtils.isNotEmpty(type)) {
							if (type.equals("string")) {
								cell.setCellType(Cell.CELL_TYPE_STRING);
								row.put(names.get(colIdx), cell
										.getStringCellValue().trim());
							} else if (type.equals("double")) {
								cell.setCellType(Cell.CELL_TYPE_NUMERIC);
								row.put(names.get(colIdx), cell
										.getNumericCellValue());
							} else if (type.equals("int")) {
								cell.setCellType(Cell.CELL_TYPE_NUMERIC);
								row.put(names.get(colIdx), (int) cell
										.getNumericCellValue());
							} else if (type.equals("date")) {
								row.put(names.get(colIdx), cell
										.getDateCellValue());
							} else {
								throw new DataProcessException("不支持的Excel格式类型");
							}
						}
					}
				}
				if (rowIdx != 0) {
					result.add(row);
				}
			}
			context.setAttribute(execBean.getResultName(), result);
		} finally {
			if (is != null) {
				is.close();
			}
		}

	}

	@Override
	public void process(ProcessorContext context) throws DataProcessException {
		if (excelExecBeanList != null) {
			for (ExcelExecBean execBean : excelExecBeanList) {
				try {
					if (StringUtils.isNotEmpty(execBean.getResourceVarName())) {
						execBean.setResource((String) PropertyUtilS
								.getNestedProperty(context.getMap(), execBean
										.getResourceVarName()));
					} else {
						if (StringUtils.isEmpty(execBean.getResource())) {
							throw new DataProcessException("Excel资源文件未指定");
						}
					}
					if (execBean.isRead()) {
						readExcel(context, execBean);
					} else {
						writeExcel(context, execBean);
					}
				} catch (Exception e) {
					throw new DataProcessException("处理Excel["
							+ execBean.getResource() + "]出错", e);
				}
			}
		}
	}

}

// $Id: ExcelProcessor.java 13776 2010-06-01 12:00:58Z wei.zhang $