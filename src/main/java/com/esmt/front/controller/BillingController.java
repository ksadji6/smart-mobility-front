package com.esmt.front.controller;

import com.esmt.front.dto.*;
import com.esmt.front.service.ApiService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/billing")
@RequiredArgsConstructor
@Slf4j // <-- AJOUTER CETTE ANNOTATION POUR LE LOG
public class BillingController {

    private final ApiService apiService;

    @GetMapping
    public String billingPage(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        String token = (String) session.getAttribute("token");

        try {
            BigDecimal balance = apiService.getBalance(userId, token);
            model.addAttribute("balance", balance != null ? balance : BigDecimal.ZERO);

            var history = apiService.getTransactionHistory(userId, token);
            model.addAttribute("history", history != null ? history : List.of());
        } catch (Exception e) {
            log.error("Erreur billing: {}", e.getMessage());
            model.addAttribute("balance", BigDecimal.ZERO);
            model.addAttribute("history", List.of());
            model.addAttribute("error", "Service de facturation indisponible.");
        }

        model.addAttribute("creditRequest", new CreditRequest());
        model.addAttribute("activeMenu", "billing");
        return "billing/wallet";
    }

    @PostMapping("/credit")
    public String credit(@ModelAttribute CreditRequest request, HttpSession session, RedirectAttributes ra) {
        String token = (String) session.getAttribute("token");
        request.setUserId((Long) session.getAttribute("userId"));
        try {
            apiService.credit(request, token);
            ra.addFlashAttribute("success", "Compte rechargé !");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Échec de la recharge.");
        }
        return "redirect:/billing";
    }
}