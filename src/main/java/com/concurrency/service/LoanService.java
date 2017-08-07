package com.concurrency.service;

import com.concurrency.exception.AppException;
import com.concurrency.model.Lender;
import com.concurrency.model.Loan;
import com.concurrency.repository.LenderRepository;
import com.concurrency.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static com.concurrency.utils.Constants.ERROR_MESSAGE;

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
                        total += (double) entry.getKey() * (double) entry.getValue();
                    }
                    Loan loan = new Loan();
                    loan.setRate(Utils.roundOne(total / loanAmount * 100));
                    loan.setRequestedAmount(loanAmount);
                    final double totalRepay = loanAmount + calculateRepayInterest(loanAmount, loan.getRate());
                    loan.setMonthlyRepayment(Utils.roundTwo(totalRepay / 36));
                    loan.setTotalRepayment(Utils.roundTwo(loan.getMonthlyRepayment() * 36));
                    return loan;
                }).exceptionally(e -> {
                    logger.error(ERROR_MESSAGE);
                    throw new AppException("100", ERROR_MESSAGE);
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
                break;
            }
        }
        if (total < loanAmount) {
            throw new AppException("100", ERROR_MESSAGE);
        }

        return lenderMap;
    }

    private double calculateRepayInterest(double loanAmount, double rate) {
        double interestAmount = 0;
        double monthlyAmount = loanAmount / 36;
        while (loanAmount > 0) {
            interestAmount += (loanAmount / 100 * rate * 3) / 36;
            loanAmount -= monthlyAmount;
        }
        return interestAmount;
    }
}
