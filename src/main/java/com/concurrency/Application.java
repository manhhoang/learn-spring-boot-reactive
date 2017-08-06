package com.concurrency;

import com.concurrency.config.AppConfig;
import com.concurrency.model.Loan;
import com.concurrency.service.LoanService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static com.concurrency.utils.Constants.LOAN_SERVICE;

public class Application {

    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) throws Exception {
        String marketFile = "lender_data.csv";
        double loanAmount = 1000;
        printLoan(getAvailableLoan(marketFile, loanAmount));
    }

    private static Loan getAvailableLoan(String marketFile, double loanAmount) {
        Loan loan = new Loan();
        final LoanService loanService = getService();
        try {
            if(loanAmount >= 1000 && loanAmount <= 15000 && (loanAmount % 100 == 0)) {
                loan = loanService.getAvailableLoan(marketFile, loanAmount).get();
            } else {
                loan.setValid(false);
            }
        } catch (Exception ex) {
            loan.setRequestedAmount(0);
        }
        return loan;
    }

    private static LoanService getService() {
        final AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);
        final LoanService loanService = (LoanService) ctx.getBean(LOAN_SERVICE);
        ctx.close();
        return loanService;
    }

    private static void printLoan(Loan loan) {
        if(!loan.isValid()) {
            System.out.println("Loan has to be of any £100 increment between £1000 and £15000 inclusive.");
            return;
        }
        if (loan.getRequestedAmount() == 0) {
            System.out.println("It is not possible to provide a quote at that time.");
        } else {
            System.out.println("Requested amount: " + String.format("%.0f", loan.getRequestedAmount()));
            System.out.println("Rate: " + String.format("%.01f", loan.getRate()) + "%");
            System.out.println("Monthly repayment: " + String.format("%.02f", loan.getMonthlyRepayment()));
            System.out.println("Total repayment: " + String.format("%.02f", loan.getTotalRepayment()));
        }
    }
}