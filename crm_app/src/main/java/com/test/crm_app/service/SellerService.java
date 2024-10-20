package com.test.crm_app.service;

import com.test.crm_app.exception.BadRequestException;
import com.test.crm_app.exception.NotFoundException;
import com.test.crm_app.exception.ConflictException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.test.crm_app.model.Seller;
import com.test.crm_app.repository.SellerRepository;

import java.util.List;

@Service
public class SellerService {

    @Autowired
    private SellerRepository sellerRepository;

    public List<Seller> getAllSellers() {
        return sellerRepository.findAll();
    }
    public Seller getSellerById(Long id) {
        if (id == null || id <= 0) {
            throw new BadRequestException("Некорректный идентификатор продавца.");
        }

        return sellerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Seller not found with id " + id));
    }
    public Seller createSeller(Seller seller) {
        if (sellerRepository.existsByContactInfo(seller.getContactInfo())) {
            throw new ConflictException("Контактная информация уже существует.");
        }
        try {
            return sellerRepository.save(seller);
        } catch (ConstraintViolationException ex) {
            throw new BadRequestException("Ошибка валидации данных.");
        }
    }

    public Seller updateSeller(Long id, Seller updatedSeller) {
        if (id == null || id <= 0) {
            throw new BadRequestException("Некорректный идентификатор продавца.");
        }

        Seller existingSeller = sellerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Seller not found with id: " + id));

        if (!existingSeller.getContactInfo().equals(updatedSeller.getContactInfo()) &&
                sellerRepository.existsByContactInfo(updatedSeller.getContactInfo())) {
            throw new ConflictException("Контактная информация уже существует.");
        }

        existingSeller.setName(updatedSeller.getName());
        existingSeller.setContactInfo(updatedSeller.getContactInfo());
        existingSeller.setRegistrationDate(updatedSeller.getRegistrationDate());

        try {
            return sellerRepository.save(existingSeller);
        } catch (ConstraintViolationException ex) {
            throw new BadRequestException("Ошибка валидации данных.");
        }
    }

    public void deleteSeller(Long id) {
        if (id == null || id < 0) {
            throw new BadRequestException("Invalid seller ID: " + id);
        }
        Seller existingSeller = sellerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Seller not found with id: " + id));
        sellerRepository.deleteById(id);
    }
}