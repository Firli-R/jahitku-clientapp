/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.id.jahitku.frontend.controller;

import co.id.jahitku.frontend.model.Order;
import co.id.jahitku.frontend.model.dto.OrderCustomer;
import co.id.jahitku.frontend.model.dto.ResponseData;
import co.id.jahitku.frontend.service.OrderService;
import java.util.HashMap;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author LENOVO
 */
@PreAuthorize("hasAnyRole('ADMIN','USER')")
@Controller
@RequestMapping("/order")
public class OrderController {

    private OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @RequestMapping("/checkout")
    @GetMapping
    public String checkout() {
        return "checkout";
    }

    @GetMapping
    public String order() {
        return "order/index";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public String orderAdmin() {
        return "orderadmin/index";
    }

    @GetMapping("/count")
    public @ResponseBody
    ResponseEntity<HashMap<String, String>> getCountStatus() {
        return ResponseEntity.ok(orderService.getCountStatus());
    }

    @GetMapping("/unpaid")
    public @ResponseBody
    ResponseEntity<List<Order>> getUnpaid() {
        return ResponseEntity.ok(orderService.getUnpaid());
    }

    @GetMapping("/recent")
    public @ResponseBody
    ResponseEntity<List<Order>> getRecent() {
        return ResponseEntity.ok(orderService.getRecent());
    }

    @PostMapping()
    public @ResponseBody
    ResponseEntity createOrder(@Valid @RequestBody List<OrderCustomer> order,
            BindingResult result) {
        if (result.hasErrors()) {
            return new ResponseEntity<>(result.getAllErrors(), HttpStatus.BAD_REQUEST);
        }

        orderService.create(order);
        return ResponseEntity.ok(new ResponseData("success", "order success"));
    }
    
    
}
