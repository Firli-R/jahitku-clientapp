/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.id.jahitku.frontend.model;

import java.time.LocalDateTime;
import lombok.Data;

/**
 *
 * @author Firli
 */
@Data
public class VerificationToken {
    private String token;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    private LocalDateTime confirmedAt;
    private User user;
}
