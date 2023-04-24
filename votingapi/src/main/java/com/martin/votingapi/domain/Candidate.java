package com.martin.votingapi.domain;

import com.martin.votingapi.domain.enums.UserRole;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "tbl_candidates")
@Data
public class Candidate {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  public Long id;
  public String name;
  public String party;
  public String image;
  public String slogan;
  public UserRole userRole;
}
