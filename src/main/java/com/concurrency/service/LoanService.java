package com.concurrency.service;

import com.concurrency.exception.AppException;
import com.concurrency.model.Lender;
import com.concurrency.model.Loan;
import com.concurrency.repository.LenderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class LoanService {

    @Autowired
    LenderRepository lenderRepository;

    private static final Logger logger = LoggerFactory.getLogger(LoanService.class);

    public CompletableFuture<Loan> getAvailableLoan(String marketFile, double loanAmount){
        return CompletableFuture.supplyAsync(() -> lenderRepository.findAllLenders(marketFile))
                .thenApply(lenders -> {
                    Loan loan = new Loan();
                    double total = 0;
                    for(Lender lender: lenders) {
                        total += lender.getAvailable();
                        if(total >= loanAmount) {
                            loan.setRequestedAmount(loanAmount);
                            break;
                        }
                    }
                    if(total < loanAmount) {
                        loan.setRequestedAmount(0);
                    }

                    return loan;
                }).exceptionally(e -> {
                    logger.error("Failed to get available loan");
                    throw new AppException("100", "Failed to get available loan");
                });
    }
}
