package com.concurrency.service;

import com.concurrency.exception.AppException;
import com.concurrency.model.Lender;
import com.concurrency.model.Loan;
import com.concurrency.repository.LenderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;

@Service
public class LoanService {

    @Autowired
    LenderRepository lenderRepository;

    private static final Logger logger = LoggerFactory.getLogger(LoanService.class);

    public CompletableFuture<Loan> getAvailableLoan(String marketFile, double loanAmount) {
        return CompletableFuture.supplyAsync(() -> lenderRepository.findAllLendersSortedByRate(marketFile))
                .thenApply(lenders -> {
                    double total = 0;
                    Map<Double, Double> lenderMap = getLenderMap(lenders, loanAmount);
                    for (Map.Entry entry : lenderMap.entrySet()) {
                        total += (double)entry.getKey() * (double)entry.getValue();
                    }
                    Loan loan = new Loan();
                    loan.setRate(total/loanAmount);
                    loan.setRequestedAmount(loanAmount);
                    return loan;
                }).exceptionally(e -> {
                    logger.error("There are no quotes available this time!");
                    throw new AppException("100", "There are no quotes available this time!");
                });
    }

    private Map<Double, Double> getLenderMap(List<Lender> lenders, double loanAmount) {
        Map<Double, Double> lenderMap = new HashMap<>();
        double total = 0;
        for (Lender lender : lenders) {
            total += lender.getAvailable();
            double availableAmount = lenderMap.getOrDefault(lender.getRate(), 0d);
            if (total <= loanAmount) {
                lenderMap.put(lender.getRate(), availableAmount + lender.getAvailable());
                if (total == loanAmount)
                    break;
            } else {
                lenderMap.put(lender.getRate(), availableAmount + lender.getAvailable() - (total - loanAmount));
            }
        }
        if (total < loanAmount) {
            throw new AppException("100", "There are no quotes available this time!");
        }

        return lenderMap;
    }
}
