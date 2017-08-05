package com.concurrency.repository;

import com.concurrency.exception.AppException;
import com.concurrency.model.Lender;
import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.HeaderColumnNameTranslateMappingStrategy;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

@Repository
public class LenderRepository {

    final HeaderColumnNameTranslateMappingStrategy<Lender> beanStrategy = new HeaderColumnNameTranslateMappingStrategy<>();

    List<Lender> lenders = new ArrayList<>();

    public List<Lender> findAllLenders(String marketFile) {
        if (!lenders.isEmpty())
            return lenders;

        beanStrategy.setType(Lender.class);
        Map<String, String> columnMapping = new HashMap<>();
        columnMapping.put("Lender", "name");
        columnMapping.put("Rate", "rate");
        columnMapping.put("Available", "available");
        beanStrategy.setColumnMapping(columnMapping);

        final CsvToBean<Lender> csvToBean = new CsvToBean<>();
        try {
            final File file = new ClassPathResource(marketFile).getFile();
            final CSVReader reader = new CSVReader(new FileReader(file));
            lenders = csvToBean.parse(beanStrategy, reader);
        } catch (IOException ex) {
            throw new AppException("100", "Failed to load market data file");
        }
        lenders.sort(Comparator.comparing(Lender::getRate));
        return lenders;
    }
}
