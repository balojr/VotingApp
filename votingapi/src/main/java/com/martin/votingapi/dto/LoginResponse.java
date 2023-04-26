package com.martin.votingapi.dto;

import com.martin.votingapi.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {

  private User user;
  private String token;
}
