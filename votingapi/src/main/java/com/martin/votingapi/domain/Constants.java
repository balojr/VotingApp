package com.martin.votingapi.domain;

public class Constants {
  public static final long ACCESS_TOKEN_VALIDITY_SECONDS = 60 * 60 * 24; //24 hours...
  public static final String SIGNING_KEY = "20201LIPACHATQNATUJENGEQAZ2wsx4rfvBHUZ2wsx4rfvBHU*";
  public static final String TOKEN_PREFIX = "Bearer ";
  public static final String HEADER_STRING = "Authorization";

  public static final String JWT_TOKEN_ISSUER = "https://cheri.natujenge.ke";

  public static final String NOTIFICATION_URL = "http://51.15.233.87:15432/message/queue";
  public static final String DEFAULT_EMAIL_SOURCE_ADDRESS = "alerts@meliora.tech";
  public static final String DEFAULT_SMS_SOURCE_ADDRESS = "SMSAfrica";
  //    http://164.92.70.172:9090/api/registration/confirm?token="+token
  public static final String DEFAULT_USER_VERIFICATION_EMAIL_TEMPLATE= "Dear $userName.  Please verify your email: $email using this OTP: $token . Click here to verify : http://51.15.141.206:9090/api/registration/confirm?token=$token " +
    "Thank you. $home.";
  public static final String DEFAULT_GUARDIAN_SMS_TEMPLATE = "Dear $guardianName. You have been successfully registered under $patient " +
    "Thank you. $home.";

}
