/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.id.jahitku.frontend.controller;

import co.id.jahitku.frontend.model.VerificationToken;
import co.id.jahitku.frontend.model.dto.RegisterData;
import co.id.jahitku.frontend.model.dto.ResponseData;
import co.id.jahitku.frontend.service.UserService;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author Firli
 */
@Controller
@AllArgsConstructor
public class UserController {

    @Autowired
    private UserService userService;
    
    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String getHome(Model model, Authentication auth) {
        model.addAttribute("name", auth);
        return "home/index";
    }
    
    @RequestMapping(value = "/homepage", method = RequestMethod.GET)
    public String getHomePage() {
        return "homepage/index";
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/dashboard", method = RequestMethod.GET)
    public String getDashboard(Model model, Authentication auth) {
        model.addAttribute("name", auth);
        return "dashboard/index";
    }
    
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String create(@Valid RegisterData registerData,
             RedirectAttributes redirect,BindingResult result) {
        if (result.hasErrors()) {
            return "user/register";
        }
        ResponseData responseData = userService.createUser(registerData);
        redirect.addFlashAttribute("message", "Registration Success");
        return "redirect:/verification-page?token=" + responseData.getMessage();
    }
    
    //gajadi
    @GetMapping("/verify-after-regist")
    public String getVerify() {
        return "user/verify_after_regis";
    }
    
    @GetMapping("/register")
    public String getRegis(@ModelAttribute RegisterData registerData,Model model) {
        model.addAttribute("registerData",registerData);
        return "user/register";
    }
    
    @GetMapping("/verification/{token}")
    public @ResponseBody
    ResponseEntity<VerificationToken> getVerificationToken(@PathVariable String token) {
        SecurityContextHolder.getContext().setAuthentication(null);

        return ResponseEntity.ok(userService.verify(token));
    }
    
    //gajadi
//    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @PostMapping("/verification")
    public @ResponseBody
    ResponseEntity<ResponseData> pushNewToken(Authentication auth) {
        return ResponseEntity.ok(userService.createNewToken(auth));
    }
    
    @GetMapping("verification-page")
    public String getVerifPage() {
        return "user/verification_page";
    }
    
    @GetMapping("/verify")
    public @ResponseBody
    ResponseEntity<ResponseData> getVerify(@RequestParam String token) {
        return ResponseEntity.ok(userService.getVerify(token));
    }
    
    @GetMapping("/validation/{username}")
    public ResponseEntity<ResponseData> getValidUsername(@PathVariable String username){
        return ResponseEntity.ok(userService.getValidUsername(username));
    }
    
    @GetMapping("/validationEmail/{email}")
    public ResponseEntity<ResponseData> getValidEmail(@PathVariable String email){
        return ResponseEntity.ok(userService.getValidEmail(email));
    }
    
    @GetMapping("/forgot-password")
    public String getForgotPassword(){
        return "user/forgot-password";
    }
    
    @GetMapping("/forgotPassword")
    public ResponseEntity<ResponseData> forgotPassword(@RequestParam String email){
        return new ResponseEntity(userService.getForgotPass(email), HttpStatus.OK);
    }
}
