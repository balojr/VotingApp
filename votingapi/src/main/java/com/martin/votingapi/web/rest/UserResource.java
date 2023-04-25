package com.martin.votingapi.web.rest;

import com.martin.votingapi.domain.User;
import com.martin.votingapi.service.UserService;
import com.martin.votingapi.web.rest.requests.RegisterUserRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping(path = "/api/user")
public class UserResource {
//  @Autowired
//  ModelMapper modelMapper;
  private final UserService userService;

  public UserResource(UserService userService) {
    this.userService = userService;
  }

  @PostMapping
  public String registerCaregiver(@RequestBody RegisterUserRequest userRequest,
                                  @AuthenticationPrincipal User userDetails) {
    return userService.signUpUser(userRequest, userDetails);
  }
  @DeleteMapping(path="/{id}")
  ResponseEntity<String> deleteUser(@PathVariable("id") Long id){
    String deletedResponse = userService.deleteUser(id);

    return new ResponseEntity<>(deletedResponse, HttpStatus.OK);
  }
}
