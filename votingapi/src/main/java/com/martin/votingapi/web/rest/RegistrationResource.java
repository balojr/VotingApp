package com.martin.votingapi.web.rest;

import com.martin.votingapi.dto.RestResponse;
import com.martin.votingapi.service.RegistrationService;
import com.martin.votingapi.web.rest.requests.RegisterUserRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/registration")
@AllArgsConstructor
public class RegistrationResource {
  @Autowired
  private final RegistrationService registrationService;

  @PostMapping
  public ResponseEntity<?> register(@RequestBody RegisterUserRequest registrationRequest) {
    try{
      registrationService.register(registrationRequest);
      return new ResponseEntity<>(new RestResponse(false,"User registered Successfully!"),
        HttpStatus.OK);
    }catch (Exception e){
      return new ResponseEntity<>(new RestResponse(true,e.getMessage()),
        HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping(path = "confirm")
  public ResponseEntity<?> confirm(@RequestParam("token") String token) {
    try{
      String response = registrationService.confirmToken(token);
      return new ResponseEntity<>(new RestResponse(false,response), HttpStatus.OK);
    } catch (Exception e){
      return new ResponseEntity<>(new RestResponse(true,e.getMessage()),
        HttpStatus.OK);
    }
  }
}
