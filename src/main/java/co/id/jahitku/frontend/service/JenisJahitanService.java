/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.id.jahitku.frontend.service;

import co.id.jahitku.frontend.model.JenisJahitan;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

/**
 *
 * @author Firli
 */
@Service
public class JenisJahitanService {
    @Value("${app.baseUrl}/jenisjahitan")
    private String url;
    
    private RestTemplate restTemplate;
    
    @Autowired
    public JenisJahitanService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    
    public List<JenisJahitan> getAll(){
        List<JenisJahitan> jenisJahitan = new ArrayList<>();
        try {
            ResponseEntity<List<JenisJahitan>> response = restTemplate.
                    exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<JenisJahitan>>() {
                    });
             if (response.getStatusCode() == HttpStatus.OK) {
                jenisJahitan = response.getBody();
                return jenisJahitan;}
        } catch (ResponseStatusException e) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, e.getMessage());
        }
        return jenisJahitan;
    }
    
    public void create(JenisJahitan jenisJahitan) {
        try {
            ResponseEntity<JenisJahitan> response = restTemplate.
                    exchange(url,
                            HttpMethod.POST, new HttpEntity<>(jenisJahitan), new ParameterizedTypeReference<JenisJahitan>() {
                    });
        } catch (ResponseStatusException e) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, e.getMessage());
        }
    }
    
    public void update(Long id, JenisJahitan jenisJahitan) {
        try {
            jenisJahitan.setId(id);
            ResponseEntity<JenisJahitan> response = restTemplate.
                    exchange(url.concat("/" + id),
                            HttpMethod.PUT, new HttpEntity<>(jenisJahitan), new ParameterizedTypeReference<JenisJahitan>() {
                    });
        } catch (ResponseStatusException e) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, e.getMessage());
        }
    }
    
    public void delete(Long id) {
        try {
            ResponseEntity<JenisJahitan> response = restTemplate.
                    exchange(url.concat("/" + id),
                            HttpMethod.DELETE, null, new ParameterizedTypeReference<JenisJahitan>() {
                    });
        } catch (ResponseStatusException e) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, e.getMessage());
        }
    }
    public JenisJahitan getById(Long id){
        JenisJahitan jenisJahitan = new JenisJahitan();
        try {
            ResponseEntity<JenisJahitan> response = restTemplate.
                    exchange(url.concat("/" + id),
                            HttpMethod.GET, null, new ParameterizedTypeReference<JenisJahitan>() {
                    });
            if(response.getStatusCode() == HttpStatus.OK){
                jenisJahitan= response.getBody();
                return jenisJahitan;
            }
        } catch (ResponseStatusException e) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, e.getMessage());
        }
        return jenisJahitan;
    }

}
