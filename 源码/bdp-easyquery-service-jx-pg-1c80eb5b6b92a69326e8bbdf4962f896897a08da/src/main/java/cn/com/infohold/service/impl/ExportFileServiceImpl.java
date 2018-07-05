package cn.com.infohold.service.impl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Service;

import bdp.commons.easyquery.param.QueryParams;
import bdp.commons.easyquery.ret.QueryFields;
import bdp.commons.easyquery.ret.QueryGroups;
import cn.com.infohold.basic.util.common.DateUtil;
import cn.com.infohold.basic.util.file.FileUtil;
import cn.com.infohold.basic.util.file.PropUtil;
import cn.com.infohold.service.IExportFileService;
import cn.com.infohold.tools.util.StringUtil;
import lombok.extern.log4j.Log4j2;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author huangdi
 * @since 2017-11-16
 */
@Log4j2(topic = "ExportFileServiceImpl")
@Service
public class ExportFileServiceImpl implements IExportFileService {

	public String geneExportFile(QueryParams queryParams, List<Map<String, Object>> exportList) throws Exception {
		log.debug(" geneExportFile  begin");
		SXSSFWorkbook swb = null;
		BufferedOutputStream bos = null;
		FileOutputStream fops = null;
		String filePath = PropUtil.getProperty("filePath");
		String fileName = queryParams.getTemplateName() + "_" + DateUtil.getServerTime(DateUtil.DEFAULT_TIME_FORMAT_DB)
				+ ".xlsx";
		try {
			long currentTimeMillis = System.currentTimeMillis();
			swb = geneSXSSFWorkbook(queryParams, exportList);

			File geneFile = new File(filePath + fileName);
			FileUtil.makeDir(filePath);
			if (!geneFile.exists()) {
				geneFile.createNewFile();
			}

			fops = new FileOutputStream(geneFile);
			bos = new BufferedOutputStream(fops);
			swb.write(bos);
			long currentTimeMillis1 = System.currentTimeMillis();
			log.debug(" geneExportFile  end {} ms", (currentTimeMillis - currentTimeMillis1));
			log.debug(" filePath + fileName {} ", (filePath + fileName));
		} catch (Exception e) {
			log.error(e);
			throw e;
		} finally {
			swb.dispose();

			if (null != fops) {
				fops.close();
			}
			if (null != bos) {
				bos.close();
			}
		}
		return fileName + "," + filePath;
	}

	public SXSSFWorkbook geneSXSSFWorkbook(QueryParams queryParams, List<Map<String, Object>> exportList)
			throws Exception {
		List<QueryFields> fields = queryParams.getFields();
		List<QueryGroups> groups = queryParams.getGroups();
		int rowno = 1;
		// 第一步，创建一个webbook，对应一个Excel文件
		SXSSFWorkbook wb = null;
		wb = new SXSSFWorkbook(100);
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		final SXSSFSheet sheet = (SXSSFSheet) wb.createSheet("导出数据");
		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
		Row row = sheet.createRow((int) 0);
		// 第四步，创建单元格，并设置值表头 设置表头居中
		Font font = wb.createFont();
		font.setFontName("黑体"); // 字体
		font.setBoldweight(Font.BOLDWEIGHT_BOLD); // 宽度

		CellStyle columnStyle = wb.createCellStyle();
		columnStyle.setFont(font);

		Cell cell = null;
		int col_line = 0;

		if (null != groups && !groups.isEmpty()) {
			for (QueryGroups group : groups) {
				if (StringUtil.isNotEmpty(group.getField())) {
					cell = row.createCell(col_line);
					cell.setCellValue(Objects.toString(group.getField_name(), ""));
					cell.setCellStyle(columnStyle);
					sheet.autoSizeColumn(0);
					col_line++;
				}
			}
		}

		for (QueryFields field : fields) { // 先写标题
			String fieldName = StringUtil.isNotEmpty(field.getAlias()) ? field.getAlias()
					: StringUtil.isNotEmpty(field.getField_name()) ? field.getField_name() : field.getTitle_name();
			cell = row.createCell(col_line);
			cell.setCellValue(Objects.toString(fieldName, ""));
			cell.setCellStyle(columnStyle);
			sheet.autoSizeColumn(0);
			col_line++;
		}

		// 生成实际数据
		try {
			// 第五步，写入实体数据，
			// final CellStyle cellStyle = wb.createCellStyle();
			rowno = 1;
			for (Map<String, Object> cellMap : exportList) {
				Row dataRow = sheet.createRow(rowno);
				Cell dataCell = null;
				int index = 0;// 标记分组是第几个

				if (null != groups && !groups.isEmpty()) {// 如果有分组就要显示分组字段
					for (int i = 0; i < groups.size(); i++) {
						if (StringUtil.isNotEmpty(groups.get(i).getField())) {
							QueryGroups group = groups.get(i);
							String name = group.getField();
							Object object = cellMap.get(name);// 每行的每一个字段
							String value = Objects.toString(object, "");
							value = "null".equals(value) ? "" : value;
							dataCell = dataRow.createCell(index);
							dataCell.setCellValue(value);
							index++;
							// dataCell.setCellStyle(cellStyle);
						}

					}
				}

				for (int i = 0; i < fields.size(); i++) {
					QueryFields field = fields.get(i);
					String name = field.getAlias();
					if (name == null || name.isEmpty()) {
						name = field.getName();
					}
					Object object = cellMap.get(name);// 每行的每一个字段
					String value = Objects.toString(object, "");
					value = "null".equals(value) ? "" : value;
					dataCell = dataRow.createCell(index);
					// CellStyle cellStyle = wb.createCellStyle();
					// DataFormat format = wb.createDataFormat();
					if (("decimal".equals(field.getField_type()))) {

						// if
						// (StringUtil.isNotEmpty(field.getField_decimal_point()))
						// {
						// cellStyle.setDataFormat(format.getFormat(getPoint(field.getField_decimal_point())));
						// } else {
						// cellStyle.setDataFormat(format.getFormat("0.00"));
						// }

						// dataCell.setCellStyle(cellStyle);
						dataCell.setCellType(Cell.CELL_TYPE_NUMERIC);
						BigDecimal b = new BigDecimal(value);
						dataCell.setCellValue(b.doubleValue());
					} else if ("number".equals(field.getField_type())) {
						// if
						// (StringUtil.isNotEmpty(field.getField_decimal_point()))
						// {
						// cellStyle.setDataFormat(format.getFormat(getPoint(field.getField_decimal_point())));
						// } else {
						// cellStyle.setDataFormat(format.getFormat("0"));
						// }

						// dataCell.setCellStyle(cellStyle);
						dataCell.setCellType(Cell.CELL_TYPE_NUMERIC);
						BigDecimal b = new BigDecimal(value);
						dataCell.setCellValue(b.doubleValue());
					} else {
						dataCell.setCellValue(value);
					}

					index++;
					// dataCell.setCellStyle(cellStyle);
				}
				sheet.autoSizeColumn(rowno);
				rowno++;
			}
			CellStyle cellStyle = wb.createCellStyle();
			DataFormat format = wb.createDataFormat();
			cellStyle.setDataFormat(format.getFormat("0.00"));
			sheet.setColumnWidth(0, 1000);
			sheet.setDefaultColumnStyle(0, cellStyle);
			sheet.setDefaultColumnStyle(1, cellStyle);
			sheet.setDefaultColumnStyle(2, cellStyle);
		} catch (Exception e) {
			log.error(e);
			throw e;
		}
		return wb;
	}

	public String getPoint(String point) {
		int p = Integer.parseInt(point);
		if (0 == p) {
			return "0";
		}
		StringBuffer sb = new StringBuffer("0.");
		for (int i = 0; i < p; i++) {
			sb.append("0");
		}

		return sb.toString();
	}

	/**
	 * 匹配是否包含数字
	 * 
	 * @param str
	 *            可能为中文，也可能是-19162431.1254，不使用BigDecimal的话，变成-1.91624311254E7
	 * @return
	 * @author yutao
	 * @date 2016年11月14日下午7:41:22
	 */
	public boolean isNumeric(String str) {
		// 该正则表达式可以匹配所有的数字 包括负数
		Pattern pattern = Pattern.compile("-?[0-9]+\\.?[0-9]*");
		String bigStr;
		try {
			bigStr = new BigDecimal(str).toPlainString();
		} catch (Exception e) {
			return false;// 异常 说明包含非数字。
		}

		Matcher isNum = pattern.matcher(bigStr); // matcher是全匹配
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}
}
