/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.id.jahitku.frontend.config;

import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author LENOVO
 */
@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate =  new RestTemplate();
        //get interceptors in rt
        List<ClientHttpRequestInterceptor> interceptors = restTemplate.getInterceptors();

        //check if interceptors in rt is null
        if(interceptors.isEmpty()) {
            //instance as arrayList
            interceptors = new ArrayList<>();
        }

        //add custom header interceptors
        interceptors.add(new RestTemplateInterceptorHeader());

        restTemplate.setInterceptors(interceptors);
        return restTemplate;
    }
}
