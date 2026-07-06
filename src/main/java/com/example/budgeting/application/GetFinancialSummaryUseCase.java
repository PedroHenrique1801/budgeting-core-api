package com.example.budgeting.application;

import com.example.budgeting.domain.Category;
import java.util.HashMap;
import java.util.Map;

public class GetFinancialSummaryUseCase {

    private final SumTransactionsByCategoryUseCase sumTransactions;

    public GetFinancialSummaryUseCase(SumTransactionsByCategoryUseCase sumTransactions) {
        this.sumTransactions = sumTransactions;
    }

    public Map<String, Long> execute() {
        Map<String, Long> summary = new HashMap<>();

        summary.put("AUTO", sumTransactions.execute(Category.AUTO));
        summary.put("GROCERIES", sumTransactions.execute(Category.GROCERIES));
        summary.put("PHARMA", sumTransactions.execute(Category.PHARMA));

        return summary;
    }
}