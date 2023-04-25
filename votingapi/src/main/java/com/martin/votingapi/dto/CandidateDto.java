package com.martin.votingapi.dto;

import com.martin.votingapi.domain.enums.UserRole;

public class CandidateDto {
  String getName();
  String getParty();
  String getImage();
  String getSlogan();
  UserRole getUserRole();
}
