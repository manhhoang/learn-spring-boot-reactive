package com.concurrency.repository;

import com.concurrency.model.Lender;
import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.HeaderColumnNameTranslateMappingStrategy;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class LenderRepository {

    final HeaderColumnNameTranslateMappingStrategy<Lender> beanStrategy = new HeaderColumnNameTranslateMappingStrategy<>();

    public List<Lender> findAllLenders() throws IOException {
        beanStrategy.setType(Lender.class);
        Map<String, String> columnMapping = new HashMap<>();
        columnMapping.put("Lender", "name");
        columnMapping.put("Rate", "rate");
        columnMapping.put("Available", "available");
        beanStrategy.setColumnMapping(columnMapping);

        final CsvToBean<Lender> csvToBean = new CsvToBean<>();
        final File file = new ClassPathResource("lender_data.csv").getFile();
        final CSVReader reader = new CSVReader(new FileReader(file));
        return csvToBean.parse(beanStrategy, reader);
    }
}
