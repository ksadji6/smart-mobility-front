package com.esmt.front.controller;

import com.esmt.front.dto.*;
import com.esmt.front.service.ApiService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final ApiService apiService;

    @GetMapping("/")
    public String root(HttpSession session) {
        return session.getAttribute("token") != null ? "redirect:/dashboard" : "redirect:/login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("registerRequest", new RegisterRequest());
        return "auth/register";
    }

    @PostMapping("/register")
    public String doRegister(@ModelAttribute RegisterRequest request, RedirectAttributes ra) {
        try {
            apiService.register(request);
            ra.addFlashAttribute("success", "Compte créé avec succès !");
            return "redirect:/login?registered=true";
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/register";
        }
    }

    @GetMapping("/login")
    public String loginPage(@RequestParam(required = false) String expired,
                            @RequestParam(required = false) String registered,
                            Model model) {
        model.addAttribute("loginRequest", new LoginRequest());
        if ("true".equals(expired)) model.addAttribute("warning", "Session expirée.");
        if ("true".equals(registered)) model.addAttribute("success", "Compte créé !");
        return "auth/login";
    }

    @PostMapping("/login")
    public String doLogin(@ModelAttribute LoginRequest request, HttpSession session, RedirectAttributes ra) {
        try {
            AuthResponse auth = apiService.login(request);
            session.setAttribute("token", auth.getToken());
            session.setAttribute("userId", auth.getUserId());
            session.setAttribute("email", auth.getEmail());
            return "redirect:/dashboard";
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}