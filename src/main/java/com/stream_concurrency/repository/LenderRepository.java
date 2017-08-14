package com.stream_concurrency.repository;

import com.stream_concurrency.exception.AppException;
import com.stream_concurrency.model.Lender;
import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.HeaderColumnNameTranslateMappingStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

@Repository
public class LenderRepository {

    private final HeaderColumnNameTranslateMappingStrategy<Lender>
            beanStrategy = new HeaderColumnNameTranslateMappingStrategy<>();

    private List<Lender> lenders = new ArrayList<>();

    private static final Logger logger = LoggerFactory.getLogger(LenderRepository.class);

    public List<Lender> findAllLendersSortedByRate(final String marketFile) {
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
            final FileInputStream fileInputStream = new FileInputStream("./" + marketFile);
            final CSVReader csvReader = new CSVReader(new InputStreamReader(fileInputStream));
            lenders = csvToBean.parse(beanStrategy, csvReader);
            fileInputStream.close();
        } catch (Exception ex) {
            logger.error("Unable to load the market data file");
            throw new AppException("100", "Failed to load market data file");
        }
        lenders.sort(Comparator.comparing(Lender::getRate));
        return lenders;
    }

    public List<Lender> streamAllLendersSortedByRate(final String marketFile) {
        if (!lenders.isEmpty())
            return lenders;

        try {
            final FileInputStream fileInputStream = new FileInputStream("./" + marketFile);
            BufferedReader br = new BufferedReader(new InputStreamReader(fileInputStream));
            lenders = br.lines()
                    .skip(1)
                    .map(columnMapping)
                    .sorted(Comparator.comparing(Lender::getRate))
                    .collect(toList());
            fileInputStream.close();
        } catch (Exception ex) {
            logger.error("Unable to load the market data file");
            throw new AppException("100", "Failed to load market data file");
        }
        return lenders;
    }

    private Function<String, Lender> columnMapping = (line) -> {
        String[] p = line.split(",");
        return new Lender(p[0], Double.parseDouble(p[1]), Double.parseDouble(p[2]));
    };
}
