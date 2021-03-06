package com.file_stream_concurrency_value_object.service;

import com.file_stream_concurrency_value_object.exception.AppException;
import com.file_stream_concurrency_value_object.model.Lender;
import com.file_stream_concurrency_value_object.model.Loan;
import com.file_stream_concurrency_value_object.repository.LenderRepository;
import com.file_stream_concurrency_value_object.utils.Utils;
import com.file_stream_concurrency_value_object.value_object.AvailableAmount;
import com.file_stream_concurrency_value_object.value_object.ImmutableAvailableAmount;
import com.file_stream_concurrency_value_object.value_object.ImmutableRate;
import com.file_stream_concurrency_value_object.value_object.Rate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static com.file_stream_concurrency_value_object.utils.Constants.ERROR_MESSAGE;

@Service
public class LoanService {

    @Autowired
    LenderRepository lenderRepository;

    private static final Logger logger = LoggerFactory.getLogger(LoanService.class);

    public CompletableFuture<Loan> getAvailableLoan(final String marketFile, final double loanAmount) {
        return CompletableFuture.supplyAsync(() -> lenderRepository.streamAllLendersSortedByRate(marketFile))
                .thenApply(lenders -> {
                    double total = 0;
                    final Map<Rate, AvailableAmount> lenderMap = getLenderMap(lenders, loanAmount);
                    for (Map.Entry entry : lenderMap.entrySet()) {
                        total += ((Rate) entry.getKey()).value() * ((AvailableAmount) entry.getValue()).value();
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
    private Map<Rate, AvailableAmount> getLenderMap(final List<Lender> lenders, final double loanAmount) {
        Map<Rate, AvailableAmount> lenderMap = new HashMap<>();
        double total = 0;
        for (Lender lender : lenders) {
            total += lender.getAvailable();
            AvailableAmount availableAmount = lenderMap.getOrDefault(lender.getRate(), ImmutableAvailableAmount.builder().value(0d).build());
            if (total <= loanAmount) {
                lenderMap.put(ImmutableRate.builder().value(lender.getRate()).build(),
                        ImmutableAvailableAmount.builder().value(availableAmount.value() + lender.getAvailable()).build());
                if (total == loanAmount)
                    break;
            } else {
                lenderMap.put(ImmutableRate.builder().value(lender.getRate()).build(),
                        ImmutableAvailableAmount.builder().value(availableAmount.value() + lender.getAvailable() - (total - loanAmount)).build());
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
