package com.example.financeApp.controller;

import com.example.financeApp.model.Expense;
import com.example.financeApp.service.ExpenseService;
import com.example.financeApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {
    @Autowired
    private ExpenseService expenseService;

    @PostMapping
    public ResponseEntity<Expense> createExpense(@RequestBody Expense expense, @RequestHeader("Authorization") String token) {
        UUID userId = getUserIdFromToken(token);
        Expense newExpense = expenseService.createExpense(expense, userId);
        return ResponseEntity.ok(newExpense);
    }

    @GetMapping
    public ResponseEntity<List<Expense>> getExpenses(@RequestHeader("Authorization") String token) {
        UUID userId = getUserIdFromToken(token);
        List<Expense> expenses = expenseService.getExpenses(userId);
        return ResponseEntity.ok(expenses);
    }

    @PutMapping("/{expenseId}")
    public ResponseEntity<Expense> updateExpense(@PathVariable UUID expenseId, @RequestBody Expense expense, @RequestHeader("Authorization") String token) {
        UUID userId = getUserIdFromToken(token);
        expense.setId(expenseId);
        Expense updatedExpense = expenseService.updateExpense(expense, userId);
        if (updatedExpense != null) {
            return ResponseEntity.ok(updatedExpense);
        }
        return ResponseEntity.status(401).body(null);
    }

    @DeleteMapping("/{expenseId}")
    public ResponseEntity<Void> deleteExpense(@PathVariable UUID expenseId, @RequestHeader("Authorization") String token) {
        UUID userId = getUserIdFromToken(token);
        expenseService.deleteExpense(expenseId, userId);
        return ResponseEntity.ok().build();
    }

    private UUID getUserIdFromToken(String token) {
        // Extract the user ID from the token
        return UserService.getUserIdFromToken(token);
    }
}
