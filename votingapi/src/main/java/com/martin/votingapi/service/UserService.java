package com.martin.votingapi.service;

import com.martin.votingapi.domain.ConfirmationToken;
import com.martin.votingapi.domain.User;
import com.martin.votingapi.domain.enums.UserRole;
import com.martin.votingapi.repository.UserRepository;
import com.martin.votingapi.web.rest.requests.RegisterUserRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
@Service
@Slf4j
@AllArgsConstructor
public class UserService implements UserDetailsService {
  private final static String USER_NOT_FOUND_MSG = "User with Email %s not found!";
  private final static String USER_EXISTS = "Email %s Taken!";

  @Autowired
  UserRepository userRepository;

//  @Autowired
//  NotificationServiceHTTPClient notificationServiceHTTPClient;

  @Autowired
  PasswordEncoder passwordEncoder;

  @Autowired
  ConfirmationTokenService confirmationTokenService;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    return userRepository.findByEmail(email)
      .orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, email)));

  }

  public String signUpUser(RegisterUserRequest registerUserRequest, User loggedInUser) {

    registerUserRequest.setUserRole(UserRole.USER);
    User newUser = new User( registerUserRequest.getUsername(),
      registerUserRequest.getEmail(),
      registerUserRequest.getPassword(),
      registerUserRequest.getUserRole(),
      registerUserRequest.getCreatedBy(),
      registerUserRequest.getCreatedAt());

    return signUpUser(newUser);

  }


  public String signUpUser(User user) {
    log.info("Signing up user {}", user);

    boolean userEmailExists = userRepository.findByEmail(user.getEmail())
      .isPresent();

    if (userEmailExists) {
      throw new IllegalStateException(String.format(USER_EXISTS, user.getEmail()));
    }

    // Add user
    String encodedPassword = passwordEncoder.encode(user.getPassword());

    // Set details
    user.setPassword(encodedPassword);

    // save the User in the database
    userRepository.save(user);
    log.info("User saved", user);

    // generate confirmation token and save it to dB
    String token = UUID.randomUUID().toString();
    ConfirmationToken confirmationToken = new ConfirmationToken(
      token,
      LocalDateTime.now(),
      LocalDateTime.now().plusMinutes(15),
      user
    );

    confirmationTokenService.saveConfirmationToken(confirmationToken);
    log.info("Confirmation token generated");

    token = confirmationToken.getToken();
    // TODO : SEND EMAIL
//    return sendEmailNotification(user, token);

        return token;
  }
  public void enableAppUser(String email) {
    User user = userRepository.findByEmail(email)
      .orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, email)));

    user.setEnabled(true); // should automatically reflect in database
  }

  public Optional<User> findByEmail(String email) {
    log.info("Request to find user with email : {}", email);

    Optional<User> user = userRepository.findByEmail(email);
    log.info("Found user : {}", user);
    return user;
  }
  public Optional<User> findById(Long id) {
    log.info("Request to find user with id : {}", id);

    Optional<User> user = userRepository.findById(id);
    log.info("Found user : {}", user);
    return user;
  }
  public User findByUsername(String username){
    return userRepository.findUserByUsername(username);
  }
  public String deleteUser(Long id) {
    boolean exist = userRepository.existsById(id);
    if (!exist) {
      throw new IllegalStateException("User with id " + id + " doesn't exist");
    }
    userRepository.deleteById(id);
    return "Deleted";
  }
}
