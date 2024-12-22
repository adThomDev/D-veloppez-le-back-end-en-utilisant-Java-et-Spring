package com.ocr.p3back.service;

import com.ocr.p3back.dao.MessageRepository;
import com.ocr.p3back.model.entity.Message;
import com.ocr.p3back.model.entity.Rental;
import com.ocr.p3back.model.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class MessageService {
  @Autowired
  private MessageRepository messageRepository;
  @Autowired
  private RentalService rentalService;
  @Autowired
  private UserService userService;

  public ResponseEntity<?> postMessage(Long rentalId, Long userId, String messageContent) {
    try {
      Rental rental = rentalService.findRentalById(rentalId);
      UserEntity user = userService.findUserById(userId);

      if (rental == null || user == null || messageContent == null || messageContent.isEmpty()) {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
      }

      Message message = new Message();
      message.setRental(rental);
      message.setUserEntity(user);
      message.setMessage(messageContent);
      message.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
      message.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
      messageRepository.save(message);

      Map<String, String> response = new HashMap<>();
      response.put("message", "Message sent with success");
      return new ResponseEntity<>(response, HttpStatus.OK);

    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}