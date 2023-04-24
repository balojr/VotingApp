package com.martin.votingapi.domain;


import com.martin.votingapi.domain.enums.UserRole;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "tbl_voters")
@Data
public class Voter {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  public Long id;
  public String name;
  public String email;
  public UserRole userRole;
  public String password;
}
