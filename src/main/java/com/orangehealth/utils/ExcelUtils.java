package com.orangehealth.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public final class ExcelUtils {

    private static final DataFormatter FORMATTER =
            new DataFormatter();

    private ExcelUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static String readCell(
            Path excelFile,
            String sheetName,
            int rowNumber,
            int columnNumber) {

        try (XSSFWorkbook workbook = openWorkbook(excelFile)) {
            XSSFSheet sheet = getRequiredSheet(workbook, sheetName);
            Row row = sheet.getRow(rowNumber);
            return getCellValue(row, columnNumber);
        } catch (IOException e) {
            throw new RuntimeException("Unable to read Excel file: " + excelFile, e);
        }
    }

    public static List<String> readRow(
            Path excelFile,
            String sheetName,
            int rowNumber) {

        try (XSSFWorkbook workbook = openWorkbook(excelFile)) {
            XSSFSheet sheet = getRequiredSheet(workbook, sheetName);
            Row row = sheet.getRow(rowNumber);
            return row == null
                    ? new ArrayList<>()
                    : readRowValues(row, getColumnCount(row));
        } catch (IOException e) {
            throw new RuntimeException("Unable to read Excel row: " + excelFile, e);
        }
    }

    public static List<List<String>> readSheet(
            Path excelFile,
            String sheetName) {

        try (XSSFWorkbook workbook = openWorkbook(excelFile)) {
            XSSFSheet sheet = getRequiredSheet(workbook, sheetName);
            List<List<String>> data = new ArrayList<>();

            for (Row row : sheet) {
                data.add(readRowValues(row, getColumnCount(row)));
            }

            return data;
        } catch (IOException e) {
            throw new RuntimeException("Unable to read Excel sheet: " + excelFile, e);
        }
    }

    public static Map<String, String> readRowAsMap(
            Path excelFile,
            String sheetName,
            int rowNumber) {

        try (XSSFWorkbook workbook = openWorkbook(excelFile)) {
            XSSFSheet sheet = getRequiredSheet(workbook, sheetName);
            Row header = sheet.getRow(0);
            Row row = sheet.getRow(rowNumber);
            return mapRow(header, row);
        } catch (IOException e) {
            throw new RuntimeException("Unable to read Excel row as map: " + excelFile, e);
        }
    }

    public static Object[][] getSheetData(
            Path excelFile,
            String sheetName) {

        try (XSSFWorkbook workbook = openWorkbook(excelFile)) {
            XSSFSheet sheet = getRequiredSheet(workbook, sheetName);
            Row header = sheet.getRow(0);
            List<Map<String, String>> rows = new ArrayList<>();

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                    rows.add(mapRow(header, row));
                }
            }

            Object[][] data = new Object[rows.size()][1];
            for (int i = 0; i < rows.size(); i++) {
                data[i][0] = rows.get(i);
            }

            return data;
        } catch (IOException e) {
            throw new RuntimeException("Unable to read Excel data provider sheet: " + excelFile, e);
        }
    }

    public static void writeCell(
            Path excelFile,
            String sheetName,
            int rowNumber,
            int columnNumber,
            String value) {

        try (XSSFWorkbook workbook = Files.exists(excelFile)
                ? openWorkbook(excelFile)
                : new XSSFWorkbook()) {

            XSSFSheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                sheet = workbook.createSheet(sheetName);
            }

            Row row = sheet.getRow(rowNumber);
            if (row == null) {
                row = sheet.createRow(rowNumber);
            }

            Cell cell = row.getCell(columnNumber);
            if (cell == null) {
                cell = row.createCell(columnNumber);
            }

            cell.setCellValue(value);

            Path parent = excelFile.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }

            try (OutputStream output = Files.newOutputStream(excelFile)) {
                workbook.write(output);
            }
        } catch (IOException e) {
            throw new RuntimeException("Unable to write Excel file: " + excelFile, e);
        }
    }

    public static int getRowCount(
            Path excelFile,
            String sheetName) {

        try (XSSFWorkbook workbook = openWorkbook(excelFile)) {
            return getRequiredSheet(workbook, sheetName).getPhysicalNumberOfRows();
        } catch (IOException e) {
            throw new RuntimeException("Unable to get Excel row count: " + excelFile, e);
        }
    }

    public static int getColumnCount(
            Path excelFile,
            String sheetName) {

        try (XSSFWorkbook workbook = openWorkbook(excelFile)) {
            Row header = getRequiredSheet(workbook, sheetName).getRow(0);
            return getColumnCount(header);
        } catch (IOException e) {
            throw new RuntimeException("Unable to get Excel column count: " + excelFile, e);
        }
    }

    public static CellType getCellType(
            Path excelFile,
            String sheetName,
            int row,
            int column) {

        try (XSSFWorkbook workbook = openWorkbook(excelFile)) {
            Row targetRow = getRequiredSheet(workbook, sheetName).getRow(row);
            Cell cell = targetRow == null ? null : targetRow.getCell(column);
            return cell == null ? CellType.BLANK : cell.getCellType();
        } catch (IOException e) {
            throw new RuntimeException("Unable to get Excel cell type: " + excelFile, e);
        }
    }

    private static XSSFWorkbook openWorkbook(Path excelFile) throws IOException {
        try (InputStream input = Files.newInputStream(excelFile)) {
            return new XSSFWorkbook(input);
        }
    }

    private static XSSFSheet getRequiredSheet(
            XSSFWorkbook workbook,
            String sheetName) {

        XSSFSheet sheet = workbook.getSheet(sheetName);
        if (sheet == null) {
            throw new IllegalArgumentException("Sheet not found: " + sheetName);
        }
        return sheet;
    }

    private static Map<String, String> mapRow(Row header, Row row) {
        Map<String, String> data = new LinkedHashMap<>();

        if (header == null || row == null) {
            return data;
        }

        int columnCount = getColumnCount(header);
        for (int i = 0; i < columnCount; i++) {
            String key = getCellValue(header, i).trim();
            if (!key.isEmpty()) {
                data.put(key, getCellValue(row, i).trim());
            }
        }

        return data;
    }

    private static List<String> readRowValues(Row row, int columnCount) {
        List<String> values = new ArrayList<>(columnCount);

        for (int i = 0; i < columnCount; i++) {
            values.add(getCellValue(row, i));
        }

        return values;
    }

    private static String getCellValue(Row row, int columnNumber) {
        if (row == null) {
            return "";
        }

        Cell cell = row.getCell(columnNumber);
        return cell == null ? "" : FORMATTER.formatCellValue(cell).trim();
    }

    private static int getColumnCount(Row row) {
        if (row == null || row.getLastCellNum() < 0) {
            return 0;
        }
        return row.getLastCellNum();
    }
}
