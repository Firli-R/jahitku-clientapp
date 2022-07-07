/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.id.jahitku.frontend.controller;

import co.id.jahitku.frontend.model.dto.LoginRequestData;
import co.id.jahitku.frontend.model.dto.ResponseData;
import co.id.jahitku.frontend.service.LoginService;
import co.id.jahitku.frontend.service.UserService;
import java.util.Map;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author Firli
 */
@Controller
@AllArgsConstructor
@Slf4j
public class LoginController {

    private LoginService loginService;
    private UserService userService;

    @GetMapping("/login")
    public String getLoginPage(LoginRequestData loginRequestData) {
        return "user/login";
    }

    @PostMapping("/login")
    public String loginPage(@Valid LoginRequestData loginRequestData,
            RedirectAttributes redirect, Model model, BindingResult bindingResult) {
        Map<String, Object> responseData = loginService.login(loginRequestData);
        log.info(bindingResult.toString());
        if (bindingResult.hasErrors()) {
            model.addAttribute("message", "error");
            return "user/login";
        }

        redirect.addFlashAttribute("name", responseData.get("nama").toString());
        if (responseData.get("role").equals("ROLE_USER")) {
            return "redirect:/home";
        } else {
            return "redirect:/dashboard";
        }
    }

    @GetMapping("/logout")
    public String logout() {
        SecurityContextHolder.getContext().setAuthentication(null);

        return "redirect:/login?logout=true";
    }
}
