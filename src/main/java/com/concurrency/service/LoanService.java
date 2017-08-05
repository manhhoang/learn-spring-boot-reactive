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
        return CompletableFuture.supplyAsync(() -> lenderRepository.findAllLenders(marketFile))
                .thenApply(lenders -> {
                    double rate = 0;
                    double available = 0;
                    List<Lender> lenderList = getLenderList(lenders, loanAmount);
                    for (int i = 0; i < lenderList.size() - 1; i++) {
                        if (i == 0) {
                            rate = (lenderList.get(i).getRate() * (lenderList.get(i + 1).getAvailable() / lenderList.get(i).getAvailable()) + lenderList.get(i + 1).getRate()) / 2;
                            available += lenderList.get(i).getAvailable() + lenderList.get(i + 1).getAvailable();
                            i = 1;
                        } else {
                            rate = (rate * (lenderList.get(i + 1).getAvailable() / available) + lenderList.get(i + 1).getRate()) / 2;
                            available += lenderList.get(i + 1).getAvailable();
                        }
                    }
                    Loan loan = new Loan();
                    loan.setRate(rate);
                    loan.setRequestedAmount(loanAmount);
                    return loan;
                }).exceptionally(e -> {
                    logger.error("There are no loan available this time!");
                    throw new AppException("100", "There are no loan available this time!");
                });
    }

    private List<Lender> getLenderList(List<Lender> lenders, double loanAmount) {
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
            throw new AppException("100", "There are no loan available this time!");
        }
        List<Lender> lendersList = new ArrayList<>();
        for (Map.Entry entry : lenderMap.entrySet()) {
            Lender lender = new Lender();
            lender.setRate((double) entry.getKey());
            lender.setAvailable((double) entry.getValue());
            lendersList.add(lender);
        }
        return lendersList;
    }
}
