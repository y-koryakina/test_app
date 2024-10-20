package com.test.crm_app.service;

import com.test.crm_app.exception.BadRequestException;
import com.test.crm_app.exception.NotFoundException;
import com.test.crm_app.model.Seller;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.test.crm_app.model.Transaction;
import com.test.crm_app.repository.TransactionRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public Transaction getTransactionById(Long id) {
        if (id == null || id <= 0) {
            throw new BadRequestException("Некорректный идентификатор транзакции.");
        }

        return transactionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Transaction not found with id " + id));
    }

    public Transaction createTransaction(Transaction transaction) {
        try {
            return transactionRepository.save(transaction);
        } catch (ConstraintViolationException ex) {
            throw new BadRequestException("Ошибка валидации данных.");
        }
    }

    public List<Transaction> getTransactionsBySellerId(Long sellerId) {
        if (sellerId == null || sellerId <= 0) {
            throw new BadRequestException("Некорректный идентификатор продавца.");
        }

        List<Transaction> transactions = transactionRepository.findBySellerId(sellerId);

        if (transactions.isEmpty()) {
            throw new NotFoundException("Транзакции не найдены для продавца с id: " + sellerId);
        }

        return transactions;
    }

    public Optional<Seller> findProductiveSeller(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate == null || endDate == null || startDate.isAfter(endDate)) {
            throw new BadRequestException("Некорректные даты.");
        }

        return transactionRepository.findProductiveSeller(startDate, endDate);
    }

    public List<Seller> findLowSalesSellers(LocalDateTime startDate, LocalDateTime endDate, BigDecimal maxAmount) {
        if (startDate == null || endDate == null || startDate.isAfter(endDate)) {
            throw new BadRequestException("Некорректные даты.");
        }

        if (maxAmount == null || maxAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("Некорректная сумма.");
        }

        return transactionRepository.findLowSalesSellers(startDate, endDate, maxAmount);
    }

}