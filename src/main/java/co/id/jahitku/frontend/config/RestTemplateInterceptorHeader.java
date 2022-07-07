/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.id.jahitku.frontend.config;

import co.id.jahitku.frontend.utility.RequestHeader;
import java.io.IOException;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.authentication.AnonymousAuthenticationToken;

/**
 *
 * @author Firli
 */
public class RestTemplateInterceptorHeader implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, 
            ClientHttpRequestExecution execution) throws IOException {
        if(RequestHeader.getAuth() != null && !(RequestHeader.getAuth() 
                instanceof AnonymousAuthenticationToken)){
            request.getHeaders().addAll(RequestHeader.createHeaders());
        }
        ClientHttpResponse response = execution.execute(request, body);
        return response;
    }
    
}
