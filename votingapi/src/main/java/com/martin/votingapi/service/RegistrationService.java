package com.martin.votingapi.service;

import com.martin.votingapi.domain.ConfirmationToken;
import com.martin.votingapi.domain.User;
import com.martin.votingapi.domain.enums.UserRole;
import com.martin.votingapi.util.EmailValidator;
import com.martin.votingapi.web.rest.requests.RegisterUserRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
@Slf4j
public class RegistrationService {
  private final static String EMAIL_NOT_VALID = "EMAIL %s IS NOT VALID";
  private final UserService userService;
  private EmailValidator emailValidator;

  private final ConfirmationTokenService confirmationTokenService;

  public String register(RegisterUserRequest userRequest) {
//
//    log.info("Registering new home {}", registrationDto);
    boolean isValidEmail = emailValidator.test(userRequest.getEmail());
//
//    // TODO: Use better Exception handling methods
    if(!isValidEmail){
      throw new IllegalStateException(String.format(EMAIL_NOT_VALID,userRequest.getEmail()));
    }
//
//    // create home
//    Home home = new Home();
//    home.setName(registrationDto.getHomeName());
//    home.setEmailAddress(registrationDto.getEmail());
//    home.setMsisdn(registrationDto.getMsisdn());
//    //TO DO: set other fields...
//
//    homeService.addNewHome(home); // save to the db
//
//    log.info("home registered");

    return userService.signUpUser(
      //String fullName, String email, String password, UserRole appuserRole, Home hom
      new User(
        userRequest.getUsername(),
        userRequest.getEmail(),
        userRequest.getPassword(),
        UserRole.ADMIN,
        "system",
        userRequest.getCreatedAt()
      )
    );
  }

  @Transactional
  public String confirmToken(String token){
    ConfirmationToken confirmationToken = confirmationTokenService.getToken(token).orElseThrow(() ->
      new IllegalStateException("Token not Found!"));

    if(confirmationToken.getConfirmedAt() != null){
      throw new IllegalStateException("Email Already already confirmed!");
    }

    LocalDateTime expiredAt = confirmationToken.getExpiresAt();

    if(expiredAt.isBefore(LocalDateTime.now())){
      throw new IllegalStateException("Token Expired!");
    }

    confirmationTokenService.setConfirmedAt(token);

    userService.enableAppUser(confirmationToken.getUser().getEmail());

    return "Confirmed";

  }
}
