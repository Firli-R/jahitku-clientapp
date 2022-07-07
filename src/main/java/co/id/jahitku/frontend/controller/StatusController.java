/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.id.jahitku.frontend.controller;

import co.id.jahitku.frontend.model.Order;
import co.id.jahitku.frontend.model.dto.PaymentRequest;
import co.id.jahitku.frontend.service.OrderService;
import co.id.jahitku.frontend.service.PaymentService;
import com.midtrans.httpclient.error.MidtransError;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.extras.springsecurity5.auth.Authorization;

/**
 *
 * @author LENOVO
 */
@PreAuthorize("hasAnyRole('ADMIN','USER')")
@Controller
@RequestMapping("/status")
public class StatusController {

    private OrderService orderService;
    private PaymentService paymentService;

    @Autowired
    public StatusController(OrderService orderService, PaymentService paymentService) {
        this.orderService = orderService;
        this.paymentService = paymentService;
    }

    @GetMapping
    public String getStatus() {
        return "status/index";
    }

    @GetMapping("/get-all")
    public @ResponseBody
    ResponseEntity<List<Order>> getAll() {
        return ResponseEntity.ok(orderService.getAll());
    }

    @GetMapping("/get-orders")
    public @ResponseBody
    ResponseEntity<List<Order>> getOrdersByUsername(Authorization auth) {
        return ResponseEntity.ok(orderService.getOrdersByUsername());
    }

    @PostMapping
    public ResponseEntity<?> createPaymentToken(@RequestBody PaymentRequest paymentRequest) throws MidtransError {

        paymentService.create(paymentRequest);
        return ResponseEntity.ok(paymentService.createPaymentToken(paymentRequest));
    }
}
