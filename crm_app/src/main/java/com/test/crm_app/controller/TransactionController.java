package com.test.crm_app.controller;
import com.test.crm_app.exception.NotFoundException;
import com.test.crm_app.repository.SellerRepository;
import com.test.crm_app.repository.TransactionRepository;
import com.test.crm_app.service.TransactionService;
import com.test.crm_app.model.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private TransactionRepository transactionRepository;


    @GetMapping
    public List<Transaction> getAllTransactions() {
        return transactionService.getAllTransactions();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable Long id) {
        Transaction transaction = transactionService.getTransactionById(id);
        if (transaction != null) {
            return ResponseEntity.ok(transaction);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Transaction> createTransaction(@RequestBody Transaction transaction) {
        Seller seller = sellerRepository.findById(transaction.getSeller().getId())
                .orElseThrow(() -> new NotFoundException("Seller not found"));
        transaction.setSeller(seller); // Устанавливаем продавца
        Transaction savedTransaction = transactionRepository.save(transaction);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTransaction);
    }

    @GetMapping("/seller/{sellerId}")
    public List<Transaction> getTransactionsBySeller(@PathVariable Long sellerId) {
        return transactionService.getTransactionsBySellerId(sellerId);
    }

    @GetMapping("/seller/productive")
    public ResponseEntity<Seller> getProductiveSeller(@RequestParam LocalDateTime startDate,
                                                      @RequestParam LocalDateTime endDate) {
        Optional<Seller> productiveSeller = transactionService.findProductiveSeller(startDate, endDate);
        if (productiveSeller.isPresent()) {
            return ResponseEntity.ok(productiveSeller.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/seller/low-sales")
    public List<Seller> getLowSalesSellers(@RequestParam LocalDateTime startDate,
                                           @RequestParam LocalDateTime endDate,
                                           @RequestParam BigDecimal maxAmount) {
        return transactionService.findLowSalesSellers(startDate, endDate, maxAmount);
    }
}
