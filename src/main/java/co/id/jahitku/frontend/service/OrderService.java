/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.id.jahitku.frontend.service;

import co.id.jahitku.frontend.model.Order;
import co.id.jahitku.frontend.model.dto.OrderCustomer;
import co.id.jahitku.frontend.model.dto.OrderData;
import java.util.ArrayList;
import java.util.HashMap;
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
 * @author
 */
@Service
public class OrderService {

    @Value("${app.baseUrl}/order")
    private String url;

    private RestTemplate restTemplate;

    @Autowired
    public OrderService(RestTemplate restTemplate) {
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

    public void update(Long id, Order order) {
        try {
            order.setId(id);
            ResponseEntity<Order> response = restTemplate.
                    exchange(url.concat("/" + id),
                            HttpMethod.PUT, new HttpEntity<>(order), new ParameterizedTypeReference<Order>() {
                    });
        } catch (ResponseStatusException e) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, e.getMessage());
        }
    }

    public List<Order> getOrdersByUsername() {
        List<Order> orders = new ArrayList<>();
        try {
            ResponseEntity<List<Order>> response = restTemplate.
                    exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<Order>>() {
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

    public HashMap<String, String> getCountStatus() {
        HashMap<String, String> countStatus = new HashMap<>();
        try {
            ResponseEntity<HashMap<String, String>> response = restTemplate.
                    exchange(url.concat("/count"), HttpMethod.GET, null, new ParameterizedTypeReference<HashMap<String, String>>() {
                    });
            if (response.getStatusCode() == HttpStatus.OK) {
                countStatus = response.getBody();
                return countStatus;
            }
        } catch (ResponseStatusException e) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, e.getMessage());
        }
        return countStatus;
    }

    public List<Order> getUnpaid() {
        List<Order> orders = new ArrayList<>();
        try {
            ResponseEntity<List<Order>> response = restTemplate.
                    exchange(url.concat("/unpaid"), HttpMethod.GET, null, new ParameterizedTypeReference<List<Order>>() {
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

    public List<Order> getRecent() {
        List<Order> orders = new ArrayList<>();
        try {
            ResponseEntity<List<Order>> response = restTemplate.
                    exchange(url.concat("/recent"), HttpMethod.GET, null, new ParameterizedTypeReference<List<Order>>() {
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

    public void create(List<OrderCustomer> orderData) {
        try {
            ResponseEntity<OrderData> response = restTemplate.
                    exchange(url,
                            HttpMethod.POST, new HttpEntity<>(orderData), new ParameterizedTypeReference<OrderData>() {
                    });
        } catch (ResponseStatusException e) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, e.getMessage());
        }
    }

    
}
