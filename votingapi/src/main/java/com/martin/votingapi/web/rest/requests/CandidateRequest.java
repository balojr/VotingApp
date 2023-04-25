package com.martin.votingapi.web.rest.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CandidateRequest {
  private String name;
  private String party;
  private String image;
  private String slogan;
  private String userRole;
}
