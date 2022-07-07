/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.id.jahitku.frontend.service;

import co.id.jahitku.frontend.model.VerificationToken;
import co.id.jahitku.frontend.model.dto.RegisterData;
import co.id.jahitku.frontend.model.dto.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

/**
 *
 * @author Firli
 */
@Service
public class UserService {

    @Value("${app.baseUrl}/user")
    private String url;

    @Autowired
    private RestTemplate restTemplate;

    public ResponseData createUser(RegisterData registerData) {
        ResponseData responseData = new ResponseData();
        try {
            ResponseEntity<ResponseData> response = restTemplate.exchange(
                    url.concat("/register"),
                    HttpMethod.POST,
                    new HttpEntity<>(registerData),
                    new ParameterizedTypeReference<ResponseData>() {
            }
            );
            if (response.getStatusCode() == HttpStatus.CREATED) {
                responseData = response.getBody();
                return responseData;
            }

        } catch (ResponseStatusException e) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Internal Server Error" + e.getMessage());
        }
        return responseData;
    }

    public VerificationToken verify(String token) {
        VerificationToken verifToken = new VerificationToken();
        try {
            ResponseEntity<VerificationToken> response = restTemplate.exchange(url.concat("/verify-page/" + token),
                    HttpMethod.GET, null, new ParameterizedTypeReference<VerificationToken>() {
            });
            if (response.getStatusCode() == HttpStatus.OK) {
                verifToken = response.getBody();
                return verifToken;
            }
        } catch (ResponseStatusException e) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Internal Server Error" + e.getMessage());
        }
        return verifToken;
    }

    public ResponseData createNewToken(Authentication auth) {
        ResponseData responseData = new ResponseData();
        try {
            ResponseEntity<ResponseData> response = restTemplate.exchange(url.concat("/verify-page"), HttpMethod.POST,
                    new HttpEntity<>(auth), new ParameterizedTypeReference<ResponseData>() {
            });
            if (response.getStatusCode() == HttpStatus.OK) {
                responseData = response.getBody();
                return responseData;
            }
        } catch (ResponseStatusException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
        return responseData;
    }

    public ResponseData getVerify(String token) {
        ResponseData responseData = new ResponseData();

        try {
            ResponseEntity<ResponseData> response = restTemplate.exchange(url.concat("/verify?token=" + token),
                    HttpMethod.GET, null, new ParameterizedTypeReference<ResponseData>() {
            });
            if (response.getStatusCode() == HttpStatus.OK) {
                responseData = response.getBody();
                return responseData;
            }
        } catch (ResponseStatusException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
        return responseData;
    }

    public ResponseData getValidUsername(String username) {
        ResponseData responseData = new ResponseData();
        try {
            ResponseEntity<ResponseData> response = restTemplate.exchange(url.concat("/validation/" + username), HttpMethod.GET,
                    null, new ParameterizedTypeReference<ResponseData>() {
            });
            if (response.getStatusCode() == HttpStatus.OK) {
                responseData = response.getBody();
                return responseData;
            } else {
                return new ResponseData("INTERNAL_SERVER_ERROR", "Terdapat Masalah pada jaringan anda");
            }
        } catch (ResponseStatusException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());

        }
    }

    public ResponseData getValidEmail(String email) {
        ResponseData responseData = new ResponseData();
        try {
            ResponseEntity<ResponseData> response = restTemplate.exchange(url.concat("/validationEmail/" + email), HttpMethod.GET,
                    null, new ParameterizedTypeReference<ResponseData>() {
            });
            if (response.getStatusCode() == HttpStatus.OK) {
                responseData = response.getBody();
                return responseData;
            } else {
                return new ResponseData("INTERNAL_SERVER_ERROR", "Terdapat Masalah pada jaringan anda");
            }
        } catch (ResponseStatusException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());

        }
    }

    public ResponseData getForgotPass(String email) {
        ResponseData responseData = new ResponseData();
        try {
            ResponseEntity<ResponseData> response = restTemplate.exchange(url.concat("/forgotPassword?email=" + email), 
                    HttpMethod.GET,
                    null, new ParameterizedTypeReference<ResponseData>() {
            });
            if (response.getStatusCode() == HttpStatus.OK) {
                responseData = response.getBody();
                return responseData;
            }else{
                return new ResponseData("error", "email not valid");
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
