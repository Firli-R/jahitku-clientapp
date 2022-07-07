/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.id.jahitku.frontend.service;

import co.id.jahitku.frontend.model.JenisJahitan;
import co.id.jahitku.frontend.model.dto.ResponseData;
import co.id.jahitku.frontend.model.dto.UpdateData;
import co.id.jahitku.frontend.model.dto.UserData;
import lombok.AllArgsConstructor;
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
public class ProfileService {

    @Value("${app.baseUrl}/user")
    private String url;

    private RestTemplate restTemplate;
    
    @Autowired
    public ProfileService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public UserData getByUsername() {
        UserData userData = new UserData();
        try {
            ResponseEntity<UserData> response = restTemplate.
                    exchange(url.concat("/get/username"), HttpMethod.GET, null, new ParameterizedTypeReference<UserData>() {
                    });

            if (response.getStatusCode() == HttpStatus.OK) {
                userData = response.getBody();
                return userData;
            }
        } catch (ResponseStatusException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

        return userData;
    }
    
    public void update(UserData userData) {
        try {
            ResponseEntity<UserData> response = restTemplate.
                    exchange(url,
                            HttpMethod.PUT, new HttpEntity<>(userData), new ParameterizedTypeReference<UserData>() {
                    });
        } catch (ResponseStatusException e) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, e.getMessage());
        }
    }
    
    public void updatePassword(UpdateData updateData, Authentication auth){
        try {
            updateData.setUsername(auth.getName());
            ResponseEntity<UpdateData> response = restTemplate.
                    exchange(url.concat("/gantiPassword"),
                            HttpMethod.PUT, new HttpEntity<>(updateData), new ParameterizedTypeReference<UpdateData>() {
                    });
        } catch (ResponseStatusException e) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, e.getMessage());
        }
    }
    public ResponseData updateEmail(UpdateData updateData,Authentication auth){
        ResponseData responseData = new ResponseData();
        try {
            updateData.setUsername(auth.getName());
            ResponseEntity<ResponseData> response = restTemplate.
                    exchange(url.concat("/gantiEmail"),
                            HttpMethod.PUT, new HttpEntity<>(updateData), new ParameterizedTypeReference<ResponseData>() {
                    });
            if(response.getStatusCode() == HttpStatus.OK){
                responseData = response.getBody();
                return responseData;
            }
        } catch (ResponseStatusException e) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, e.getMessage());
        }
         return responseData;
    }

}
