package com.ocr.p3back.controller;

import com.ocr.p3back.model.dto.message.MessageRequest;
import com.ocr.p3back.model.dto.message.MessageResponse;
import com.ocr.p3back.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin
@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

  private final MessageService messageService;

  @PostMapping("")
  @Operation(description = "Sends a message to the rental's owner", responses = {
      @ApiResponse(description = "Success", responseCode = "200", content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = MessageResponse.class),
          examples = @ExampleObject(value = "{\"message\":\"Message send with success\"}")
      )),
      @ApiResponse(description = "Bad Request", responseCode = "400"),
      @ApiResponse(description = "Unauthorized", responseCode = "401")
  },
      security = {@SecurityRequirement(name = "bearerAuth")},
      requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
          description = "The message sent",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = MessageRequest.class),
              examples = @ExampleObject(
                  name = "Example Request",
                  value = "{\"message\":\"mon message\",\"user_id\":1,\"rental_id\":1}"
              )
          )
      )
  )
  public ResponseEntity<?> sendMessage(@RequestBody MessageRequest messageRequest) {

    return messageService.postMessage(messageRequest.getRental_id(), messageRequest.getUser_id(), messageRequest.getMessage());
  }
}
