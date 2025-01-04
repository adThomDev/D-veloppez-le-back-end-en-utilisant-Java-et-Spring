package com.ocr.p3back.model.dto.message;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageRequest {
  String message;
  Long user_id;
  Long rental_id;
}
