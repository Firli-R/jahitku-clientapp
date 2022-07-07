/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.id.jahitku.frontend.service;

import co.id.jahitku.frontend.model.JenisJahitanOrder;
import co.id.jahitku.frontend.model.Order;
import co.id.jahitku.frontend.model.dto.JenisOrderData;
import co.id.jahitku.frontend.model.dto.Jjod;
import co.id.jahitku.frontend.model.dto.OrderData;
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
public class OrderListService {

    @Value("${app.baseUrl}/order")
    private String url;

    private RestTemplate restTemplate;
    @Autowired
    public OrderListService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<Order> getAll() {
        List<Order> orders = new ArrayList<>();
        try {
            ResponseEntity<List<Order>> response = restTemplate.
                    exchange(url.concat("/admin"), HttpMethod.GET, null, new ParameterizedTypeReference<List<Order>>() {
                    });
            if (response.getStatusCode() == HttpStatus.OK) {
                orders = response.getBody();
                return orders;
            }
        } catch (ResponseStatusException e) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, e.getMessage());
        }
        return orders;
    }
    
    public void update(Long orderId, JenisOrderData order) {
        try {
            ResponseEntity<Order> response = restTemplate.
                    exchange(url.concat("/" + orderId),
                            HttpMethod.PUT, new HttpEntity<>(order), new ParameterizedTypeReference<Order>() {
                    });
        } catch (ResponseStatusException e) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, e.getMessage());
        }
    }
    
    public ResponseEntity<JenisJahitanOrder> updateJenisJahitanOrder(Jjod jjod) {
        try {
            ResponseEntity<JenisJahitanOrder> response = restTemplate.
                    exchange(url.concat("/order-jahitan/update"),
                            HttpMethod.PUT, new HttpEntity<>(jjod), new ParameterizedTypeReference<JenisJahitanOrder>() {
                    });
            return response;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, e.getMessage());
        }
    }
   
}
