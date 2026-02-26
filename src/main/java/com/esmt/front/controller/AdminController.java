package com.esmt.front.controller;

import com.esmt.front.service.ApiService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final ApiService apiService;

    @GetMapping("/users")
    public String listUsers(HttpSession session, Model model) {
        String token = (String) session.getAttribute("token");
        // Optionnel : vérifier ici si session.getAttribute("role") est bien "ADMIN"

        model.addAttribute("users", apiService.getAllUsers(token));
        model.addAttribute("activeMenu", "admin-users");
        return "admin/users";
    }
}