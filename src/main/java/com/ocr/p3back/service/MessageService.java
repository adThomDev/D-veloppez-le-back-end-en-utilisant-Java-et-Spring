package com.ocr.p3back.service;

import com.ocr.p3back.dao.MessageRepository;
import com.ocr.p3back.model.dto.message.MessageRequest;
import com.ocr.p3back.model.dto.message.MessageResponse;
import com.ocr.p3back.model.entity.Message;
import com.ocr.p3back.model.entity.Rental;
import com.ocr.p3back.model.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Service
public class MessageService {

  @Autowired
  private MessageRepository messageRepository;

  @Autowired
  private RentalService rentalService;

  @Autowired
  private UserService userService;

  /**
   * Sends a message to a rental's owner.
   *
   * @param messageRequest contains the message, and ID's of the rental and its owner.
   * @return A message indicating success, or a 400 status if the message could not be sent.
   */
  public ResponseEntity<MessageResponse> postMessage(MessageRequest messageRequest) {
    Long rentalId = messageRequest.getRental_id();
    Long userId = messageRequest.getUser_id();
    String messageContent = messageRequest.getMessage();

    try {
      Rental rental = rentalService.findRentalById(rentalId);
      UserEntity user = userService.findUserById(userId);

      if (rental == null || user == null || messageContent == null || messageContent.isEmpty()) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
      }

      Message message = new Message();
      message.setRental(rental);
      message.setUserEntity(user);
      message.setMessage(messageContent);
      message.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
      message.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
      messageRepository.save(message);
      MessageResponse messageResponse = new MessageResponse("Message send with success");

      return ResponseEntity.status(HttpStatus.OK).body(messageResponse);
    } catch (Exception e) {

      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
  }
}