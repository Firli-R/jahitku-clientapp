/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.id.jahitku.frontend.utility;

import java.nio.charset.Charset;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 *
 * @author Firli
 */
public class RequestHeader {
    public static HttpHeaders createHeaders(){
        return new HttpHeaders(){
            {
                String auth = getAuth().getName() + ":" + getAuth().getCredentials().toString();
                    byte[] encodeAuth = Base64.encodeBase64(
                            auth.getBytes(Charset.forName("US-ASCII")));
                    String authHeader = "Basic " + new String(encodeAuth);
                    set("Authorization", authHeader);
            }
        };
    }
    
    public static Authentication getAuth(){
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
