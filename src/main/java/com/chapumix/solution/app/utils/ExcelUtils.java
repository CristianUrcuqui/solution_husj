package com.chapumix.solution.app.utils;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;

public class ExcelUtils {

	public static CellStyle createTitleCellStyle(Workbook workbook) {
				
		CellStyle cellStyle = workbook.createCellStyle();
		// horizontally
		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		// Vertical alignment
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		// background color
		cellStyle.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());

		Font headerFont1 = workbook.createFont();
		// Bold font
		headerFont1.setBold(true);
		// Font type
		headerFont1.setFontName("Blackbody");
		// font size
		headerFont1.setFontHeightInPoints((short) 15);
		cellStyle.setFont(headerFont1);
		
		return cellStyle;
	}

	public static CellStyle createHeadCellStyle(Workbook workbook) {
		        
		CellStyle cellStyle = workbook.createCellStyle();
        //Set wrap
        cellStyle.setWrapText(true);
        //Set background color
        cellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        //horizontally
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        //Vertical alignment
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setBottomBorderColor(IndexedColors.BLACK.index);
        //bottom
        cellStyle.setBorderBottom(BorderStyle.THIN);
        //border-left
        cellStyle.setBorderLeft(BorderStyle.THIN);
        //border-right
        cellStyle.setBorderRight(BorderStyle.THIN);
        //border-top
        cellStyle.setBorderTop(BorderStyle.THIN);

        //Create font styles
        Font headerFont = workbook.createFont();
        //Bold font
        headerFont.setBold(true);
        //Font type
        headerFont.setFontName("Arial");
        //font size
        headerFont.setFontHeightInPoints((short)10);
        //Add a font style to the title style
        cellStyle.setFont(headerFont);

        return cellStyle;        
	}

	public static CellStyle createContentCellStyle(Workbook workbook) {
		CellStyle cellStyle = workbook.createCellStyle();
        //horizontally
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        //Vertical centering
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        //Set wrap
        cellStyle.setWrapText(true);
        //border-top
        cellStyle.setBorderTop(BorderStyle.THIN);
        //bottom
        cellStyle.setBorderBottom(BorderStyle.THIN);
        //border-left
        cellStyle.setBorderLeft(BorderStyle.THIN);
        //border-right
        cellStyle.setBorderRight(BorderStyle.THIN);

        //Set font
        Font font = workbook.createFont();
        font.setColor((short)8);
        font.setFontHeightInPoints((short)12);	

        return cellStyle;
	}
	
	
	public static CellStyle createContentCellStyleMiPres(Workbook workbook) {
		CellStyle cellStyle = workbook.createCellStyle();
        //horizontally
        cellStyle.setAlignment(HorizontalAlignment.JUSTIFY);
        //Vertical centering
        cellStyle.setVerticalAlignment(VerticalAlignment.JUSTIFY);
        //Set wrap
        cellStyle.setWrapText(true);
        //border-top
        cellStyle.setBorderTop(BorderStyle.THIN);
        //bottom
        cellStyle.setBorderBottom(BorderStyle.THIN);
        //border-left
        cellStyle.setBorderLeft(BorderStyle.THIN);
        //border-right
        cellStyle.setBorderRight(BorderStyle.THIN);

        //Set font
        Font font = workbook.createFont();
        font.setColor((short)8);
        font.setFontHeightInPoints((short)12);	

        return cellStyle;
	}
	

}
