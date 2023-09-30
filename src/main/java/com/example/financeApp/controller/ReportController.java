package com.example.financeApp.controller;

import com.example.financeApp.model.Expense;
import com.example.financeApp.service.ReportService;
import com.example.financeApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/filter")
    public ResponseEntity<List<Expense>> filterExpensesByDate(@RequestParam("startDate") String startDate,
                                                              @RequestParam("endDate") String endDate,
                                                              @RequestHeader("Authorization") String token) {
        UUID userId = getUserIdFromToken(token);
        List<Expense> filteredExpenses = reportService.filterExpensesByDate(userId, LocalDate.parse(startDate), LocalDate.parse(endDate));
        return ResponseEntity.ok(filteredExpenses);
    }

    @GetMapping("/total")
    public ResponseEntity<BigDecimal> getTotalExpense(@RequestParam("startDate") String startDate,
                                                      @RequestParam("endDate") String endDate,
                                                      @RequestHeader("Authorization") String token) {
        UUID userId = getUserIdFromToken(token);
        BigDecimal totalExpense = reportService.calculateTotalExpense(userId, LocalDate.parse(startDate), LocalDate.parse(endDate));
        return ResponseEntity.ok(totalExpense);
    }

    private UUID getUserIdFromToken(String token) {
        // Extract the user ID from the token
        return UserService.getUserIdFromToken(token);
    }
}
