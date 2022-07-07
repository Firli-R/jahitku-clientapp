/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.id.jahitku.frontend.model.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Firli
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterData {

    @NotEmpty(message = "Masukan email anda")
    private String nama;
    
    @NotEmpty(message = "Masukan nomor anda")
    private String phone;
    
    @NotEmpty(message = "masukan email anda")
    @Email(message="masukan email yang valid")
    private String email;
    
    @NotEmpty(message="masukan alamat anda")
    private String alamat;
    
    @NotEmpty(message="Masukan Username yang uniq")
    private String username;
    
    @NotEmpty(message="masukan password anda")
    @Size(min=8,message="karakter password minimal 8")
    private String password;
}
