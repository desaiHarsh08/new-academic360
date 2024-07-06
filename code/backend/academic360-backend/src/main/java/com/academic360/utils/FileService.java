package com.academic360.utils;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileService {

    public List<StudentRow> readFile(String fileName) throws IOException, CsvException {
        String filePath = Paths.get("temp", fileName).toString();
        if (fileName.endsWith(".csv")) {
            return readCSVFile(filePath);
        } else if (fileName.endsWith(".xlsx")) {
            return readExcelFile(filePath);
        } else {
            throw new IllegalArgumentException("Unsupported file type: " + fileName);
        }
    }

    private List<StudentRow> readCSVFile(String filePath) throws IOException, CsvException {
        List<StudentRow> studentRowList = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            List<String[]> allData = reader.readAll();
            for (String[] row : allData) {
                if (row.length >= 20) { // Adjust the number based on the actual CSV structure
                    StudentRow studentRow = new StudentRow(
                        parseInteger(row[0]),
                        null,
                        null,
                        null,
                        null,
                        row[1],
                        row[2],
                        row[3],
                        parseInteger(row[4]),
                        row[5],
                        parseDouble(row[6]),
                        row[7],
                        parseFloat(row[8]),
                        parseInteger(row[9]),
                        parseInteger(row[10]),
                        parseFloat(row[11]),
                        parseInteger(row[12]),
                        parseFloat(row[13]),
                        row[14],
                        parseFloat(row[15]),
                        parseFloat(row[16]),
                        parseFloat(row[17]),
                        row[18],
                        row[19],
                        row[20]
                    );
                    studentRowList.add(studentRow);
                }
            }
        }
        return studentRowList;
    }

    private List<StudentRow> readExcelFile(String filePath) throws IOException {
        List<StudentRow> studentRowList = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getPhysicalNumberOfCells() >= 20) { // Adjust the number based on the actual Excel structure
                    StudentRow studentRow = new StudentRow(
                        parseInteger(getCellValue(row, 0)),
                        null,
                        null,
                        null,
                        null,
                        getCellValue(row, 1),
                        getCellValue(row, 2),
                        getCellValue(row, 3),
                        parseInteger(getCellValue(row, 4)),
                        getCellValue(row, 5),
                        parseDouble(getCellValue(row, 6)),
                        getCellValue(row, 7),
                        parseFloat(getCellValue(row, 8)),
                        parseInteger(getCellValue(row, 9)),
                        parseInteger(getCellValue(row, 10)),
                        parseFloat(getCellValue(row, 11)),
                        parseInteger(getCellValue(row, 12)),
                        parseFloat(getCellValue(row, 13)),
                        getCellValue(row, 14),
                        parseFloat(getCellValue(row, 15)),
                        parseFloat(getCellValue(row, 16)),
                        parseFloat(getCellValue(row, 17)),
                        getCellValue(row, 18),
                        getCellValue(row, 19),
                        getCellValue(row, 20)
                    );
                    studentRowList.add(studentRow);
                }
            }
        }
        return studentRowList;
    }

    private String getCellValue(Row row, int cellNum) {
        Cell cell = row.getCell(cellNum);
        return cell == null ? "" : cell.toString();
    }

    private Integer parseInteger(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Double parseDouble(String value) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Float parseFloat(String value) {
        try {
            return Float.parseFloat(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
