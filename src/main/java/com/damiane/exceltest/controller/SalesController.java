package com.damiane.exceltest.controller;

import com.damiane.exceltest.entity.Sales;
import com.damiane.exceltest.repository.SalesRepository;
import com.damiane.exceltest.service.ExcelService;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/sales")
public class SalesController {

    @Autowired
    private ExcelService excelService;

    @Autowired
    private SalesRepository salesRepository;

    @PostMapping("/import")
    public ResponseEntity<String> importSalesData(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is empty. Please upload a valid Excel file.");
        }

        String contentType = file.getContentType();
        if (!"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet".equals(contentType)) {
            return ResponseEntity.badRequest().body("Invalid file type. Please upload an Excel file (.xlsx).");
        }

        excelService.importFromExcel(file);
        return ResponseEntity.ok("Data imported successfully.");
    }

    @GetMapping("/data")
    public ResponseEntity<List<Sales>> getAllSalesData() {
        List<Sales> salesData = salesRepository.findAll();
        if (salesData.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(salesData);
    }

    @GetMapping("/export")
    public ResponseEntity<byte[]> exportSalesData() throws IOException {
        Workbook workbook = excelService.exportToExcel();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=sales_report.xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(out.toByteArray());
    }
}
