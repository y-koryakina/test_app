package com.test.crm_app.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.test.crm_app.model.Seller;

public interface SellerRepository extends JpaRepository<Seller, Long> {
    Seller findByName(String name);
    boolean existsByContactInfo(String contactInfo);

    Seller findByContactInfo(String mail);
}
