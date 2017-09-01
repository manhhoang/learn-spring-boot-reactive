package com.file_stream_concurrency.service;

import com.file_stream_concurrency.exception.AppException;
import com.file_stream_concurrency.model.Lender;
import com.file_stream_concurrency.model.Loan;
import com.file_stream_concurrency.repository.LenderRepository;
import com.file_stream_concurrency.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static com.file_stream_concurrency.utils.Constants.ERROR_MESSAGE;

@Service
public class LoanService {

    @Autowired
    LenderRepository lenderRepository;

    private static final Logger logger = LoggerFactory.getLogger(LoanService.class);

    public CompletableFuture<Loan> getAvailableLoan(final String marketFile, final double loanAmount) {
        return CompletableFuture.supplyAsync(() -> lenderRepository.streamAllLendersSortedByRate(marketFile))
                .thenApply(lenders -> {
                    double total = 0;
                    final Map<Double, Double> lenderMap = getLenderMap(lenders, loanAmount);
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

    /**
     * Get the lender rate and amount available for loan.
     *
     * @param lenders List of lender sorted by rate from market data.
     * @param loanAmount The loan amount
     * @return map of list lender rate and amount
     */
    private Map<Double, Double> getLenderMap(final List<Lender> lenders, final double loanAmount) {
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

    /**
     * Get total interest payment in 36 months
     *
     * @param loanAmount
     * @param rate
     * @return total interest payment
     */
    private double calculateRepayInterest(final double loanAmount, final double rate) {
        double totalAmount = loanAmount;
        double interestAmount = 0;
        double monthlyAmount = loanAmount / 36;
        while (totalAmount > 0) {
            interestAmount += (totalAmount / 100 * rate * 3) / 36;
            totalAmount -= monthlyAmount;
        }
        return interestAmount;
    }
}
