package com.martin.votingapi.repository;

import com.martin.votingapi.domain.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken,Long> {
  Optional<ConfirmationToken> findByToken(String token);

  Optional<ConfirmationToken> findByUserEmailAndToken(String email, String token);
}
