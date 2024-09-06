package com.damiane.exceltest.service;

import com.damiane.exceltest.entity.Sales;
import com.damiane.exceltest.repository.SalesRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Slf4j
@Service
public class ExcelService {

    @Autowired
    private SalesRepository salesRepository;

    public void importFromExcel(MultipartFile file) throws IOException {
        List<Sales> salesList = new ArrayList<>();
        Workbook workbook = new XSSFWorkbook(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rows = sheet.iterator();

        if (rows.hasNext()) {
            rows.next();
        }

        while (rows.hasNext()) {
            Row currentRow = rows.next();

            if (currentRow.getPhysicalNumberOfCells() >= 2) {
                Cell categoryCell = currentRow.getCell(0);
                String productCategory = categoryCell != null ? categoryCell.getStringCellValue() : "";

                Cell salesCell = currentRow.getCell(1);
                double totalSales = 0;
                if (salesCell != null) {
                    switch (salesCell.getCellType()) {
                        case NUMERIC:
                            totalSales = salesCell.getNumericCellValue();
                            log.info("Numeric value read: {}", totalSales);
                            break;
                        case STRING:
                            try {
                                totalSales = Double.parseDouble(salesCell.getStringCellValue());
                                log.info("Parsed string value read: {}", totalSales);
                            } catch (NumberFormatException e) {
                                log.error("Invalid string format for sales value: {}", salesCell.getStringCellValue());
                            }
                            break;
                        default:
                            log.warn("Unexpected cell type: {}", salesCell.getCellType());
                    }
                } else {
                    log.warn("Total Sales cell is null for product category: {}", productCategory);
                }

                salesList.add(new Sales(productCategory, totalSales));
            }
        }

        salesRepository.saveAll(salesList);
        workbook.close();
    }

    public Workbook exportToExcel() {
        List<Sales> salesList = salesRepository.findAll();
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sales Report");

        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Product Category");
        header.createCell(1).setCellValue("Total Sales");

        int rowNum = 1;
        for (Sales sale : salesList) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(sale.getProductCategory());
            row.createCell(1).setCellValue(sale.getTotalSales());
        }

        return workbook;
    }
}

