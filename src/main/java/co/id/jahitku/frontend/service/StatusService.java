/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.id.jahitku.frontend.service;

import co.id.jahitku.frontend.model.JenisJahitan;
import co.id.jahitku.frontend.model.Order;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

/**
 *
 * @author LENOVO
 */
@Service
public class StatusService {
    @Value("${app.baseUrl}/status")
    private String url;
    
    private RestTemplate restTemplate;
    
    @Autowired
    public StatusService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    
    public List<Order> getAll(){
        List<Order> order = new ArrayList<>();
        try {
            ResponseEntity<List<Order>> response = restTemplate.
                    exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<Order>>() {
                    });
             if (response.getStatusCode() == HttpStatus.OK) {
                order = response.getBody();
                return order;}
        } catch (ResponseStatusException e) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, e.getMessage());
        }
        return order;
    }
    
}
