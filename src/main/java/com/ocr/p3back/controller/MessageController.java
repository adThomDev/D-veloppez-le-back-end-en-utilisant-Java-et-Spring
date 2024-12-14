package com.ocr.p3back.controller;

import com.ocr.p3back.model.entity.Message;
import com.ocr.p3back.model.entity.Rental;
import com.ocr.p3back.model.entity.UserEntity;
import com.ocr.p3back.service.MessageService;
import com.ocr.p3back.service.RentalService;
import com.ocr.p3back.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/api/messages")
public class MessageController {

  @Autowired
  private MessageService messageService;

  @Autowired
  private RentalService rentalService;

  @Autowired
  private UserService userService;

  @PostMapping("/")
  public ResponseEntity<Map<String, String>> sendMessage(@RequestBody Map<String, Object> payload) {
    Long rentalId = ((Number) payload.get("rental_id")).longValue();
    Long userId = ((Number) payload.get("user_id")).longValue();
    String messageContent = (String) payload.get("message");

    Map<String, String> response = new HashMap<>();

    try {
      Rental rental = rentalService.findById(rentalId);
      if (rental == null) {
        response.put("error", "Rental not found");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
      }

      UserEntity user = userService.findUserById(userId);
      if (user == null) {
        response.put("error", "User not found");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
      }

      if (messageContent == null || messageContent.isEmpty()) {
        response.put("error", "Bad request: message content is missing");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
      }

      // Create and save the message
      Message message = new Message();
      message.setRental(rental);
      message.setUser(user);
      message.setMessage(messageContent);
      message.setCreatedAt(LocalDateTime.now());
      message.setUpdatedAt(LocalDateTime.now());

      messageService.saveMessage(message);

      response.put("message", "Message sent with success");
      return new ResponseEntity<>(response, HttpStatus.OK);

    } catch (Exception e) {
      response.put("error", "An unexpected error occurred: " + e.getMessage());
      return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
