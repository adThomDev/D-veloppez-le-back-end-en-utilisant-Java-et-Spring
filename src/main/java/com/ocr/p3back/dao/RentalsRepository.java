package com.ocr.p3back.dao;

import com.ocr.p3back.model.entity.Rental;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RentalsRepository extends JpaRepository<Rental, Long> {
}
