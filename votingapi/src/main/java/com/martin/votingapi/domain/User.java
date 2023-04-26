package com.martin.votingapi.domain;

import com.martin.votingapi.domain.enums.UserRole;
import javax.persistence.*;
import javax.persistence.Table;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

@Entity
@Table(name = "tbl_users")
@Data
public class User  implements UserDetails {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String username;
  private String password;
  private String email;
  private UserRole userRole;
  @Column(nullable = false)
  private LocalDateTime createdAt;
  @Column(nullable = false)
  private String createdBy;

  public User(String username, String password, String email, UserRole userRole, String createdBy, String createdAt) {
    this.username = username;
    this.password = password;
    this.email = email;
    this.userRole = userRole;
    this.createdBy = createdBy;
    this.createdAt = LocalDateTime.now();
  }
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
