/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.id.jahitku.frontend.controller.rest;

import co.id.jahitku.frontend.model.dto.PaymentRequest;
import co.id.jahitku.frontend.model.dto.ResponseData;
import co.id.jahitku.frontend.model.dto.StatusBayarData;
import co.id.jahitku.frontend.service.PaymentService;
import com.midtrans.httpclient.error.MidtransError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author LENOVO
 */
@Controller
@RequestMapping("/payment")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @PostMapping
    public ResponseEntity<?> createPaymentToken(@RequestBody
                PaymentRequest paymentRequest) throws MidtransError {

        paymentService.create(paymentRequest);
        return ResponseEntity.ok(paymentService.createPaymentToken(paymentRequest));
    }
    
    @GetMapping
    public String getCheckout (){
        return "checkout/index";
    }
    
//    @PostMapping("/pembayaran")
//    public String create(@RequestBody PaymentRequest paymentRequest){
//        paymentService.create(paymentRequest);
//        return "success"; 
//    }
    @PutMapping("/status-pembayaran")
    public ResponseEntity<ResponseData> updateStatusPembayaran(@RequestBody StatusBayarData sbd){
        paymentService.update(sbd);
        return new ResponseEntity(new ResponseData("success", "Status Terupdate"), HttpStatus.OK);
    }
}
