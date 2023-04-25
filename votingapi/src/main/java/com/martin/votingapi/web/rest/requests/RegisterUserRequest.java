package com.martin.votingapi.web.rest.requests;

import com.martin.votingapi.domain.enums.UserRole;
import lombok.*;

@AllArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
@ToString
public class RegisterUserRequest {
  private final String username;
  private final String email;
  private final String password;
  private UserRole userRole;
  private String createdBy;
  private String createdAt;
}
