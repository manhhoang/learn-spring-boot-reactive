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
            loan = loanService.getAvailableLoan(marketFile, loanAmount).get();
        } catch (Exception ex) {
            loan.setRequestedAmount(0);
        }
        return loan;
    }

    private static LoanService getService() {
        final AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);
        final LoanService loanService = (LoanService)ctx.getBean(LOAN_SERVICE);
        ctx.close();
        return loanService;
    }

    private static void printLoan(Loan loan) {
        if(loan.getRequestedAmount() == 0) {
            System.out.println("There are no quotes available this time!");
        } else {
            System.out.println("Requested amount: " + loan.getRequestedAmount());
            System.out.println("Rate: " + Math.round(loan.getRate() * 100 * 100.0)/100.0 + "%");
            System.out.println("Monthly repayment: " + loan.getMonthlyRepayment());
            System.out.println("Total repayment: " + loan.getTotalRepayment());
        }
    }
}