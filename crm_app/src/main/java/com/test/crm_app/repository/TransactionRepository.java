package com.test.crm_app.repository;

import com.test.crm_app.model.Seller;

import org.springframework.data.jpa.repository.JpaRepository;
import com.test.crm_app.model.Transaction;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query("SELECT t.seller FROM Transaction t " +
            "WHERE t.transactionDate BETWEEN :startDate AND :endDate " +
            "GROUP BY t.seller " +
            "ORDER BY SUM(t.amount) DESC")
    Optional<Seller> findProductiveSeller(LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT t.seller FROM Transaction t " +
            "WHERE t.transactionDate BETWEEN :startDate AND :endDate " +
            "GROUP BY t.seller " +
            "HAVING SUM(t.amount) < :maxAmount")
    List<Seller> findLowSalesSellers(LocalDateTime startDate, LocalDateTime endDate, BigDecimal maxAmount);

    List<Transaction> findBySellerId(Long sellerId);
}
