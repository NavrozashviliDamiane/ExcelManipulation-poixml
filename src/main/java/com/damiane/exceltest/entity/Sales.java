package com.damiane.exceltest.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "sales")
public class Sales {
    @Id
    private String id;
    private String productCategory;
    private double totalSales;

    public Sales(String productCategory, double totalSales) {
        this.productCategory = productCategory;
        this.totalSales = totalSales;
    }

}

