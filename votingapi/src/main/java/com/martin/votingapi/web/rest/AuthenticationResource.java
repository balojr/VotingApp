package com.martin.votingapi.web.rest;

import com.martin.votingapi.domain.User;
import com.martin.votingapi.domain.UserSession;
import com.martin.votingapi.dto.LoginResponse;
import com.martin.votingapi.dto.RestResponse;
import com.martin.votingapi.repository.UserSessionRepository;
import com.martin.votingapi.security.JWTTokenUtil;
import com.martin.votingapi.service.UserService;
import com.martin.votingapi.web.rest.requests.LoginRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/token")
@Slf4j
public class AuthenticationResource {

  @Autowired
  UserService userService;

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private UserSessionRepository userSessionRepository;
  @Autowired
  JWTTokenUtil jwtTokenUtil;

  @PostMapping
  public ResponseEntity<?> generateToken(@RequestBody LoginRequest login) {
    try {

      log.info("login request for username {}", login.getUsername());

      User user = userService.findByUsername(login.getUsername());

      if (user == null) {
        String message = String.format("User %s not found", login.getUsername());
        log.info("login error message: {}", message);
        return new ResponseEntity<>(new RestResponse(true, "Username or password invalid"),
          HttpStatus.UNAUTHORIZED);
      }

      //authenticate the resource
      final Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(login.getUsername(), login.getPassword()));
      SecurityContextHolder.getContext().setAuthentication(authentication);

      log.info("user has been authenticated successfully");
      UserSession userSession = userSessionRepository.findByUser(user);

      if (userSession == null) {
        userSession = new UserSession();
        userSession.setUser(user);
        userSession.setLoggedIn(0);
        userSessionRepository.save(userSession);
      }

      log.info("user has been authenticated successfully");
      String token = jwtTokenUtil.generateToken(user);
      //TODO set permissions here
      userSession.setLoggedIn(1);
      userSessionRepository.save(userSession);


      return new ResponseEntity(new LoginResponse(user, token), HttpStatus.OK);

    } catch (AuthenticationException authe) {

      String message = String.format("Authentication error for  %s", login.getUsername());
      log.error("Authentication error for  {} Ex: {}", login.getUsername(), authe.getMessage());
      return new ResponseEntity<>(new RestResponse(true, "Wrong username/Password."),
        HttpStatus.UNAUTHORIZED);

    } catch (Exception e) {

      String message = String.format("Internal server error on login  %s", login.getUsername());
      log.error("Error occurred while calling generateToken ", e);
      return new ResponseEntity(new RestResponse(true, "Error occurred, try again later"),
        HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/logout/{token}")
  public ResponseEntity<?> logout(@PathVariable("token") String token) {
    try {

      log.info("Received a request to log out for token: {}", token);

      if (token == null || token.equals("")) {
        return new ResponseEntity<>(new RestResponse(true,
          "Failed to logout, token is empty"), HttpStatus.OK);
      }

      String username = jwtTokenUtil.getUsernameUnlimitedSkew(token);
      log.info("retrieved the username: {}", username);


      User user = userService.findByUsername(username);

      if (log.isDebugEnabled()) {
        log.debug("Received a request to log out {}", username);
      }

      log.info("retrieved the user: {}", user.getUsername());


      UserSession userSession = userSessionRepository.findByUser(user);

      if (userSession == null) {
        return new ResponseEntity<>(new RestResponse(false, "User session not found"),
          HttpStatus.OK);
      }

      if (userSession.getLoggedIn() == 0) {
        return new ResponseEntity<>(new RestResponse(false, "User already logged out"),
          HttpStatus.OK);
      }

      userSession.setLoggedIn(0);
      userSessionRepository.save(userSession);
      log.info("logout successful for session: {}", userSession);

      return new ResponseEntity<>(new RestResponse(false, "User logged out"),
        HttpStatus.OK);


    } catch (Exception e) {
      log.error("Error occurred while calling {} for {} Ex: {}", "logout", token, e);
      return new ResponseEntity<>(new RestResponse(true, "Failed to Logout, Try Later"),
        HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
