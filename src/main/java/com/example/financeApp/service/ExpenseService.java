package com.example.financeApp.service;

import com.example.financeApp.model.Expense;
import com.example.financeApp.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
public class ExpenseService {
    @Autowired
    private ExpenseRepository expenseRepository;

    public Expense createExpense(Expense expense, UUID userId) {
        expense.setUserId(userId);
        return expenseRepository.save(expense);
    }

    public List<Expense> getExpenses(UUID userId) {
        return expenseRepository.findByUserId(userId);
    }

    public Expense updateExpense(Expense expense, UUID userId) {
        Expense existingExpense = expenseRepository.findById(expense.getId()).orElse(null);
        if (existingExpense != null && existingExpense.getUserId().equals(userId)) {
            expense.setUserId(userId);  // Explicitly set the userId
            return expenseRepository.save(expense);
        }
        return null;
    }



    public void deleteExpense(UUID expenseId, UUID userId) {
        Expense expense = expenseRepository.findById(expenseId).orElse(null);
        if (expense != null && expense.getUserId().equals(userId)) {
            expenseRepository.delete(expense);
        }
    }
}
