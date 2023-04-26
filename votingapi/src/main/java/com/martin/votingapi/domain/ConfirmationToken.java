package com.martin.votingapi.domain;

import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_confirmation_tokens")
@Data
@NoArgsConstructor
public class ConfirmationToken {
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Id
  private Long id;
  @Column(nullable = false) // created column cannot be null
  private String token;
  @Column(nullable = false)
  private LocalDateTime createdAt;
  @Column(nullable = false)
  private LocalDateTime expiresAt;
  private LocalDateTime confirmedAt;

  @ManyToOne
  @JoinColumn(
    nullable = false,
    name = "user_id"
  )
  private User user;

  public ConfirmationToken(String token, LocalDateTime createdAt, LocalDateTime expiresAt, User user) {
    this.token = token;
    this.createdAt = createdAt;
    this.expiresAt = expiresAt;
    this.user = user;
  }
}
