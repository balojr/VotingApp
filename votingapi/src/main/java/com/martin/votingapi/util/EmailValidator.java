package com.martin.votingapi.util;

import org.springframework.stereotype.Service;

import java.util.function.Predicate;

@Service
public class EmailValidator implements Predicate<String> {
  private static final String EMAIL_REGEX =
    "^[a-zA-Z0-9_+&*-]+(?:\\." +
      "[a-zA-Z0-9_+&*-]+)*@" +
      "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
      "A-Z]{2,7}$";

    @Override
    public boolean test(String s) {
      if (s == null || s.isEmpty()) {
        return false;
      }
      return s.matches(EMAIL_REGEX);
    }
}
