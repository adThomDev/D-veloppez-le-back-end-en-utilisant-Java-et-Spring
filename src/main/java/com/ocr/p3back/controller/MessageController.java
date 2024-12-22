package com.ocr.p3back.controller;

import com.ocr.p3back.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

  private final MessageService messageService;

  @PostMapping("/")
  public ResponseEntity<?> sendMessage(@RequestBody Map<String, Object> payload) {
    Long rentalId = ((Number) payload.get("rental_id")).longValue();
    Long userId = ((Number) payload.get("user_id")).longValue();
    String messageContent = (String) payload.get("message");

    return messageService.postMessage(rentalId, userId, messageContent);
  }
}
