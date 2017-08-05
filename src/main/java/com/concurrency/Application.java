package com.concurrency;

import com.concurrency.config.AppConfig;
import com.concurrency.model.Loan;
import com.concurrency.service.LoanService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static com.concurrency.utils.Constants.LOAN_SERVICE;

public class Application {

    public static void main(String[] args) throws Exception {
        String marketFile = "lender_data.csv";
        double loanAmount = 1000;
        printLoan(getAvailableLoan(marketFile, loanAmount));
    }

    private static Loan getAvailableLoan(String marketFile, double loanAmount) throws Exception{
        final LoanService loanService = getService();
        return loanService.getAvailableLoan(marketFile, loanAmount).get();
    }

    private static LoanService getService() {
        final AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);
        final LoanService loanService = (LoanService)ctx.getBean(LOAN_SERVICE);
        ctx.close();
        return loanService;
    }

    private static void printLoan(Loan loan) {
        if(loan.getRequestedAmount() == 0) {
            System.out.println("There are no loan available this time!");
        }
        System.out.println();
    }
}