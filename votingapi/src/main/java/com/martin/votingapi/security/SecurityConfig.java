package com.martin.votingapi.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{



//      @Bean
//      CorsConfigurationSource corsConfigurationSource() {
//        final CorsConfiguration config = new CorsConfiguration();
//        config.setAllowCredentials(true);
//        String[] allowedOrigins = new String[1];
//        allowedOrigins[0]= "http://localhost:8080";
//        config.setAllowedOrigins(Arrays.asList(allowedOrigins));
//        config.addAllowedHeader("*");
//        config.addAllowedMethod("*");
//        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", config);
//        return source;
//    }
  @Bean
  public FilterRegistrationBean<JWTAuthenticationFilter> jwtAuthenticationFilterRegistration() {
    FilterRegistrationBean<JWTAuthenticationFilter> registrationBean = new FilterRegistrationBean<>();
    registrationBean.setFilter(jwtAuthenticationFilterBean());
    registrationBean.addUrlPatterns("/api/**/"); // or any other URL pattern that requires authentication
    registrationBean.setName("jwtAuthenticationFilter");
    registrationBean.setOrder(1);
    return registrationBean;
  }

  @Bean
  public JWTAuthenticationFilter jwtAuthenticationFilterBean() {
    return new JWTAuthenticationFilter();
  }

  @Autowired
  private JWTAuthenticationEntryPoint unauthorizedHandler;

  @Override
  protected void configure(HttpSecurity http) throws Exception {

    http.cors().and()
      .csrf().disable()
      .authorizeRequests()
      .antMatchers( "/api/token/logout/**",
        "/api/token", //eq to login
        "/api/hello",
        "/api/token/refresh-token", //refresh token - we will not use it for now...
        "/api/registration", //used for registering a home - get a token[sent via email or sms]
        "/api/registration/confirm" //step 1 reg - confirm with token to activate account
        
      )
      .permitAll().anyRequest().authenticated().and().exceptionHandling()
      .authenticationEntryPoint(unauthorizedHandler).and().sessionManagement()
      .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//    http.addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);
  }

  @Bean
  public JWTAuthenticationFilter authenticationTokenFilterBean() throws Exception {
    return new JWTAuthenticationFilter();
  }

  @Override
  @Bean
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Bean
  public PasswordEncoder encoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }
}
