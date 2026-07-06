package com.example.budgeting.application;

import com.example.budgeting.domain.Category;
import com.example.budgeting.domain.TransactionRepository;
import org.springframework.stereotype.Service;

@Service
public class SumTransactionsByCategoryUseCase {

    private final TransactionRepository transactionRepository;

    public SumTransactionsByCategoryUseCase(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public Long execute(Category category) {
        return transactionRepository.sumAmountByCategory(category);
    }
}