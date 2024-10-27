package com.ocr.p3back.dao;

import com.ocr.p3back.model.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessagesRepository extends JpaRepository<Message, Long> {
}
