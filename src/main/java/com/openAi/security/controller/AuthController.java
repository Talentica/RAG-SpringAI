package com.openAi.security.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.openAi.security.exceptions.CustomRuntimeException;
import com.openAi.security.model.JwtRequest;
import com.openAi.security.model.UserRequest;
import com.openAi.security.model.WebClientResponse;
import com.openAi.security.repository.CustomerCredentialRepository;
import com.openAi.security.entity.CustomerCredentials;
import com.openAi.security.service.UserService;
import com.openAi.security.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

  private static final String ERROR_DUE_TO_INVALID_EMAIL = "Bad/Unauthorised Request error due to Invalid email";
  private static final String COOKIE = "Set-Cookie";
  private static final String BEARER = "Bearer ";
  private final JwtUtils jwtUtils;
  private final HttpServletResponse httpServletResponse;
  private final HttpServletRequest httpServletRequest;
  private final UserService userService;
  private final CustomerCredentialRepository customerCredentialRepository;

  @Value("${security.jwtExpirationMs}")
  private Long jwtExpirationMs;
  private final PasswordEncoder passwordEncoder;

  @PostMapping("/getTokenUserAuth")
  public ResponseEntity<WebClientResponse> authenticateCustomer(@RequestBody UserRequest userRequest){
      String user = userRequest.getUser();
      List<CustomerCredentials> credentialsList = customerCredentialRepository.findAllByUserNameOrUserEmail(user,user);
      if(credentialsList.isEmpty()){
        log.error(ERROR_DUE_TO_INVALID_EMAIL);
        return ResponseEntity.status(HttpStatus.NON_AUTHORITATIVE_INFORMATION).body(WebClientResponse.builder()
                .reason("Incorrect Username")
                .build());
      }
      CustomerCredentials customerCredentials = credentialsList.get(0);
      if(!passwordEncoder.matches(userRequest.getPassword(),customerCredentials.getPassword())){
        return ResponseEntity.status(HttpStatus.NON_AUTHORITATIVE_INFORMATION).body(WebClientResponse.builder()
                .reason("Incorrect Password")
                .build());
      }

      else{
        String jwt = jwtUtils.getJWTToken(customerCredentials.getUserEmail());
        userService.saveUserLog(customerCredentials.getUserEmail());


        var cookie = getAuthCookie(jwt);
        httpServletResponse.addHeader(COOKIE, cookie.toString());

        customerCredentials.setLoginCount(Optional.ofNullable(customerCredentials.getLoginCount()).orElse(0) + 1);
        customerCredentialRepository.save(customerCredentials);

        return ResponseEntity.ok(WebClientResponse.builder()
                .givenName(customerCredentials.getUserName())
                .displayName(customerCredentials.getUserName())
                .mail(customerCredentials.getUserEmail())
                .isResetRequired(!customerCredentials.getIsReset())
                .token(BEARER + jwt).build());
      }
  }

  @PostMapping("/resetPassword")
  public ResponseEntity<WebClientResponse> resetPassword(@RequestBody UserRequest userRequest){
    String user = userRequest.getUser();
    List<CustomerCredentials> credentialsList = customerCredentialRepository.findAllByUserNameOrUserEmail(user,user);
    if(credentialsList.isEmpty()){
      log.error(ERROR_DUE_TO_INVALID_EMAIL);
      return ResponseEntity.status(HttpStatus.NON_AUTHORITATIVE_INFORMATION).body(WebClientResponse.builder()
              .reason("Incorrect Username")
              .build());
    }
    String actualPwd = credentialsList.get(0).getPassword();
    String password = userRequest.getOldPassword();

    if(!passwordEncoder.matches(password,actualPwd)){
      return ResponseEntity.status(HttpStatus.NON_AUTHORITATIVE_INFORMATION).body(WebClientResponse.builder()
              .reason("Incorrect Password")
              .build());
    }

    if(userRequest.getPassword().isBlank()){
      return ResponseEntity.status(HttpStatus.NON_AUTHORITATIVE_INFORMATION).body(WebClientResponse.builder()
              .reason("Enter New Password")
              .build());
    }

    credentialsList.get(0).setPassword(passwordEncoder.encode(userRequest.getPassword()));
    credentialsList.get(0).setPwdGeneratedDate(new Date());
    credentialsList.get(0).setIsReset(true);

    customerCredentialRepository.save(credentialsList.get(0));

    String jwt = jwtUtils.getJWTToken(credentialsList.get(0).getUserEmail());
    userService.saveUserLog(credentialsList.get(0).getUserEmail());

    if(Boolean.TRUE.equals(credentialsList.get(0).getIsReset())) {
      var cookie = getAuthCookie(jwt);
      httpServletResponse.addHeader(COOKIE, cookie.toString());
    }

    return ResponseEntity.ok(WebClientResponse.builder()
              .givenName(credentialsList.get(0).getUserName())
              .displayName(credentialsList.get(0).getUserName())
              .mail(credentialsList.get(0).getUserEmail())
              .isResetRequired(!credentialsList.get(0).getIsReset())
              .token(BEARER + jwt).build());
  }

  @GetMapping("/initialLogin")
  public ResponseEntity<Boolean> ifInitialLogin(@RequestParam String email) {
    List<CustomerCredentials> credentialsList = customerCredentialRepository.findAllByUserEmail(email);
    if(credentialsList.isEmpty()){
      throw new IllegalArgumentException(ERROR_DUE_TO_INVALID_EMAIL);
    }
    return ResponseEntity.ok(credentialsList.get(0).getLoginCount() == 1);
  }

  @PostMapping("/getTokenGoogleAuth")
  public ResponseEntity<WebClientResponse> authenticateGoogleUser(@RequestBody JwtRequest jwtRequest) {
    WebClient client = WebClient.create();
    JsonNode response = client.get()
            .uri("https://www.googleapis.com/oauth2/v3/userinfo")
            .headers(headers -> {
              headers.setBearerAuth(jwtRequest.getToken());
              headers.setContentType(MediaType.APPLICATION_JSON);
            })
            .retrieve()
            .bodyToMono(JsonNode.class)
            .block();

    log.info(
            "Profile successfully validated from google against user {}",
            Objects.requireNonNull(response).get("email").asText());

    String mail = response.get("email").asText();
    if (mail == null) {
      log.error(ERROR_DUE_TO_INVALID_EMAIL);
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    String jwt = jwtUtils.getJWTToken(mail);

    log.info("JWT token fetched successfully for valid email: {}", mail);

    userService.saveUserLog(mail);

    var cookie = getAuthCookie(jwt);
    httpServletResponse.addHeader(COOKIE, cookie.toString());

    return ResponseEntity.ok(WebClientResponse.builder()
                    .id(response.get("sub").asText())
                    .givenName(response.get("given_name").asText())
                    .displayName(response.get("name").asText())
                    .mail(mail)
                    .token(BEARER + jwt).build());
  }

  @PostMapping("/getToken")
  public ResponseEntity<WebClientResponse> authenticateUser(@RequestBody JwtRequest jwtRequest) {
    WebClient client = WebClient.create();
    WebClientResponse response =
            client
                    .get()
            .uri("https://graph.microsoft.com/v1.0/me")
            .headers(
                headers -> {
                  headers.setBearerAuth(jwtRequest.getToken());
                  headers.setContentType(MediaType.APPLICATION_JSON);
                })
            .retrieve()
            .bodyToMono(WebClientResponse.class)
            .block();
    log.info(
        "Me profile successfully validated from microsoft against user {}",
        Objects.requireNonNull(response).getMail());

    String mail = response.getMail();
    if (StringUtils.isEmpty(response.getMail())) {
      log.error(ERROR_DUE_TO_INVALID_EMAIL);
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    String jwt = jwtUtils.getJWTToken(mail);
    response.setToken(BEARER + jwt);
    log.info("JWT token fetched successfully for valid email: {}", response.getMail());

    userService.saveUserLog(mail);


    var cookie = getAuthCookie(jwt);
    httpServletResponse.addHeader(COOKIE, cookie.toString());

    return ResponseEntity.ok(response);
  }

  private ResponseCookie getAuthCookie(String authToken)  {
    URL url = null;
    try {
      url = new URL(httpServletRequest.getRequestURL().toString());
    } catch (MalformedURLException e) {
      throw new CustomRuntimeException(e.getMessage());
    }
    final String cookieName = "auth-token";
    final int expiryTime = Math.toIntExact(jwtExpirationMs / 1000);  // millis in seconds
    return ResponseCookie
            .from(cookieName,authToken)
            .secure(true)
            .httpOnly(false)
            .path("/")
            .maxAge(expiryTime)
            .domain(url.getHost().equalsIgnoreCase("localhost")?"localhost":".talentica.com")
            .sameSite("None")
            .build();

  }
}
