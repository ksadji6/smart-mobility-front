package com.esmt.front.controller;

import com.esmt.front.dto.*;
import com.esmt.front.service.ApiService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.math.BigDecimal;

@Controller
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final ApiService apiService;

    @GetMapping
    public String dashboard(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        String token = (String) session.getAttribute("token");

        UserResponse user = apiService.getUser(userId, token);
        BigDecimal balance = apiService.getBalance(userId, token);

        model.addAttribute("user", user);
        model.addAttribute("balance", balance);
        model.addAttribute("activeMenu", "dashboard");
        return "dashboard/home";
    }
}