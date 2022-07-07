/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.id.jahitku.frontend.controller;

import co.id.jahitku.frontend.model.Order;
import co.id.jahitku.frontend.model.dto.JenisOrderData;
import co.id.jahitku.frontend.model.dto.Jjod;
import co.id.jahitku.frontend.model.dto.OrderData;
import co.id.jahitku.frontend.model.dto.ResponseData;
import co.id.jahitku.frontend.service.OrderListService;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author LENOVO
 */
@PreAuthorize("hasRole('ADMIN')")
@Controller
@RequestMapping("/orderlist")
public class OrderListController {

    private OrderListService orderService;

    @Autowired
    public OrderListController(OrderListService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public String getOrderList() {
        return "orderlist/index";
    }

    @GetMapping("/get-all")
    public @ResponseBody
    ResponseEntity<List<Order>> getAllJenisJahitan() {
        return ResponseEntity.ok(orderService.getAll());
    }

    @PutMapping("/{id}")
    public @ResponseBody
    ResponseEntity update(@Valid @RequestBody JenisOrderData order, @PathVariable("id") Long id,
            BindingResult result) {
        if (result.hasErrors()) {
            return new ResponseEntity<>(result.getAllErrors(), HttpStatus.BAD_REQUEST);
        }

        orderService.update(id, order);
        return ResponseEntity.ok(new ResponseData("success", "update success"));
    }
    
    @PutMapping("/jenis-jahitan-order")
    public ResponseEntity<Jjod> updatejenisJahitanOrder(@RequestBody Jjod jjod){
        return  new ResponseEntity(orderService.updateJenisJahitanOrder(jjod), HttpStatus.OK);
    }
}
