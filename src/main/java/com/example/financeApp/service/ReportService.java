package com.example.financeApp.service;

import com.example.financeApp.model.Expense;
import com.example.financeApp.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ReportService {

    @Autowired
    private ExpenseRepository expenseRepository;

    public List<Expense> filterExpensesByDate(UUID userId, LocalDate startDate, LocalDate endDate) {
        List<Expense> expenses = expenseRepository.findByUserId(userId);
        return expenses.stream()
                .filter(expense -> expense.getDate().isAfter(startDate) && expense.getDate().isBefore(endDate))
                .collect(Collectors.toList());
    }

    public BigDecimal calculateTotalExpense(UUID userId, LocalDate startDate, LocalDate endDate) {
        List<Expense> expenses = filterExpensesByDate(userId, startDate, endDate);
        return expenses.stream()
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
