package com.damiane.exceltest.repository;

import com.damiane.exceltest.entity.Sales;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalesRepository extends MongoRepository<Sales, String> {
}
