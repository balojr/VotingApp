package com.martin.votingapi.repository;

import com.martin.votingapi.domain.User;
import com.martin.votingapi.domain.UserSession;
import org.springframework.data.repository.CrudRepository;

public interface UserSessionRepository extends CrudRepository<UserSession, Long> {

      UserSession findByUser(User admin);
}
