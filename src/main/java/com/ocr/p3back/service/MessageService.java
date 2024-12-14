package com.ocr.p3back.service;

import com.ocr.p3back.model.entity.Message;
import com.ocr.p3back.dao.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

  @Autowired
  private MessageRepository messageRepository;

  public void saveMessage(Message message) {
    messageRepository.save(message);
  }
}
