/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.id.jahitku.frontend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Firli
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseData {
    private String status;
    private String message;
}
