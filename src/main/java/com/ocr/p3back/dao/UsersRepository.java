package com.ocr.p3back.dao;

import com.ocr.p3back.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<User, Long> {
}
