package com.martin.votingapi.security;

import com.martin.votingapi.domain.Constants;
import com.martin.votingapi.domain.User;
import com.martin.votingapi.domain.UserSession;
import com.martin.votingapi.repository.UserSessionRepository;
import com.martin.votingapi.service.UserService;
import io.jsonwebtoken.ExpiredJwtException;


import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.SignatureException;

@Component

public class JWTAuthenticationFilter extends OncePerRequestFilter {
  @Autowired
  private UserDetailsService userDetailsService;

  @Autowired
  private JWTTokenUtil jwtTokenUtil;

  @Autowired
  UserService userService;

  @Autowired
  UserSessionRepository userSessionRepository;

  @Override
  protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
    throws IOException, ServletException {

    //Authorization: Bearer jwt_token
    final String header = req.getHeader(Constants.HEADER_STRING); //Bearer jwt_token
    String username = null;
    String authToken = null;
    if (header != null && header.startsWith(Constants.TOKEN_PREFIX)) {
      authToken = header.replace(Constants.TOKEN_PREFIX, ""); //jwt_token
      if(authToken != null && authToken != "") {
        try{
          username = jwtTokenUtil.getUsernameFromToken(authToken);
        } catch (final IllegalArgumentException e) {
          logger.error("An error occurred during getting username from token {}", e.getCause());
        } catch (final ExpiredJwtException e) {
          logger.info("user token has expired");
          username = jwtTokenUtil.getUsernameUnlimitedSkew(authToken);
          final User user = userService.findByEmail(username).get();
          UserSession userSession = userSessionRepository.findByUser(user);
          userSession.setLoggedIn(0);
          userSessionRepository.save(userSession);
          username = null;
        }
//        catch (final SignatureException e) {
//          logger.error("signatureException. {}", e.getCause());
//        }
      }
    } else {
      logger.warn("couldn't find bearer string, will ignore the header");
    }

    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

      final UserDetails userDetails = userDetailsService.loadUserByUsername(username);

      if (jwtTokenUtil.validateToken(authToken, userDetails)) {
        // Role role = roleService.getUserRole(userDetails.getUsername());
        final UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
          userDetails, null, userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails((javax.servlet.http.HttpServletRequest) req));
        SecurityContextHolder.getContext().setAuthentication(authentication);
      }
    }

    chain.doFilter(req, res);
  }

}
