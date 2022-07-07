/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.id.jahitku.frontend.controller;

import co.id.jahitku.frontend.model.dto.ResponseData;
import co.id.jahitku.frontend.model.dto.UpdateData;
import co.id.jahitku.frontend.model.dto.UserData;
import co.id.jahitku.frontend.service.ProfileService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author Firli
 */
@Controller
@RequestMapping("/profile")
public class ProfileController {
    
    @Autowired
    private ProfileService profileService;
    
    @GetMapping
    public String getProfile(){
        return "profile/index";
    }
    @GetMapping("/user")
    public ResponseEntity<UserData> getUser(){
        return new ResponseEntity(profileService.getByUsername(), HttpStatus.OK);
    } 
    @PutMapping("/user")
    public ResponseEntity<ResponseData> getUpdateUser(@RequestBody UserData userData){
        profileService.update(userData);
        return new ResponseEntity(new ResponseData("success", "Update Success"), HttpStatus.OK);
    }
    
    @GetMapping("/user/change-password")
    public String updatePassword(){
        return "profile/change-password";
    
    }
    @GetMapping("/user/change-email")
    public String updateEmail(UpdateData updateData){
        return "profile/change-email";
    
    }
    
    @PutMapping("/user/change-password")
    public ResponseEntity<ResponseData> getUpdatePassword(@RequestBody UpdateData updateData,Authentication auth){
        profileService.updatePassword(updateData,auth);
        return new ResponseEntity(new ResponseData("success", "Update Success"), HttpStatus.OK);
    }
    @PutMapping("/user/change-email")
    public String getUpdateEmail(@Valid UpdateData updateData,Authentication auth
            ,BindingResult result,RedirectAttributes redirect){
        if(result.hasErrors()){
            return "/user/change-email";
        }
        ResponseData responseData = profileService.updateEmail(updateData,auth);
        redirect.addFlashAttribute("message", "Pembaruan Email Berhasil");

        return "redirect:/verification-page?token="+responseData.getMessage();
    }
    
}
