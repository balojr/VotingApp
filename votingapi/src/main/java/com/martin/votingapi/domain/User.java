package com.martin.votingapi.domain;

import com.martin.votingapi.domain.enums.UserRole;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Entity
@Table(name = "tbl_users")
@Data
public class User  implements UserDetails {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String Username;
  private String password;
  private String email;
  private UserRole userRole;
  private Boolean locked = false;
  private Boolean enabled = false;

//  security
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
      SimpleGrantedAuthority grantedAuthority = new SimpleGrantedAuthority(userRole.name());

      return Collections.singletonList(grantedAuthority);
    }

    @Override
    public String getPassword() {
      return password;
    }

    @Override
    public String getUsername() {
      return email;
    }

    @Override
    public boolean isAccountNonExpired() {
      // allow you to manage and track account expiry
      return true;
    }

    @Override
    public boolean isAccountNonLocked() {
      return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
      return true;
    }

    @Override
    public boolean isEnabled() {
      return enabled;
    }

}
