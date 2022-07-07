/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.id.jahitku.frontend.service;

import co.id.jahitku.frontend.model.JenisJahitan;
import co.id.jahitku.frontend.model.dto.PaymentRequest;
import co.id.jahitku.frontend.model.dto.PembayaranData;
import co.id.jahitku.frontend.model.dto.StatusBayarData;
import com.midtrans.Midtrans;
import com.midtrans.httpclient.SnapApi;
import com.midtrans.httpclient.error.MidtransError;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
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
 * @author LENOVO
 */
@Service
public class PaymentService {

    private RestTemplate restTemplate;
    
    @Autowired
    public PaymentService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    
    @Value("${app.baseUrl}/pembayaran")
    private String url;

//    @Value("${midtrans.serverKey}")
//    private String serverKey;
    public String createPaymentToken(PaymentRequest paymentRequest) throws MidtransError {
        Midtrans.serverKey = "SB-Mid-server-NbbiKGAFxvXyP64VI7U1uSFU";
        Midtrans.isProduction = false;

        return SnapApi.createTransactionToken(requestBody(paymentRequest));
    }

    public Map<String, Object> requestBody(PaymentRequest paymentRequest) {
        UUID idRand = UUID.randomUUID();
        Map<String, Object> params = new HashMap<>();

        Map<String, Object> transactionDetails = new HashMap<>();
        transactionDetails.put("order_id", paymentRequest.getOrderId());
        transactionDetails.put("gross_amount", paymentRequest.getOrderAmount());
        params.put("transaction_details", transactionDetails);

        return params;
    }

    public void create(PaymentRequest paymentRequest) {
        PembayaranData pembayaranData = new PembayaranData();
        
        pembayaranData.setNoOrder(paymentRequest.getOrderId());
        pembayaranData.setTotalBiaya(paymentRequest.getOrderAmount());
        pembayaranData.setStatusPembayaran("SUDAH_DIBAYAR");
        try {
            ResponseEntity<PembayaranData> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    new HttpEntity<>(pembayaranData),
                    new ParameterizedTypeReference<PembayaranData>() {
            }
            );
        } catch (ResponseStatusException ex) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, ex.getMessage());
        }
    }
    
    public void update(StatusBayarData statusBayarData) {
        try {
            ResponseEntity<JenisJahitan> response = restTemplate.
                    exchange(url.concat("/update-status-bayar"),
                            HttpMethod.PUT, new HttpEntity<>(statusBayarData), new ParameterizedTypeReference<JenisJahitan>() {
                    });
        } catch (ResponseStatusException e) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, e.getMessage());
        }
    }
    

}
