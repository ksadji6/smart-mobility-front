package com.esmt.front.controller;

import com.esmt.front.dto.UserResponse;
import com.esmt.front.service.ApiService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/pass")
@RequiredArgsConstructor
public class PassController {

    private final ApiService apiService;

    @GetMapping
    public String passDetails(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        String token = (String) session.getAttribute("token");
        UserResponse user = apiService.getUser(userId, token);
        model.addAttribute("pass", user.getMobilityPass());
        model.addAttribute("activeMenu", "pass");
        return "pass/detail";
    }

    @PostMapping("/suspend") // Sera accessible via /pass/suspend
    public String suspend(@RequestParam String passNumber, HttpSession session, RedirectAttributes ra) {
        String token = (String) session.getAttribute("token");
        try {
            apiService.suspendPass(passNumber, token);
            ra.addFlashAttribute("success", "Pass suspendu avec succès.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Erreur lors de la suspension.");
        }
        return "redirect:/dashboard";
    }
    @PostMapping("/reactivate") // Sera accessible via /pass/reactivate
    public String reactivate(@RequestParam String passNumber, HttpSession session, RedirectAttributes ra) {
        String token = (String) session.getAttribute("token");
        try {
            apiService.reactivatePass(passNumber, token);
            ra.addFlashAttribute("success", "Pass réactivé avec succès.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Erreur lors de la réactivation : " + e.getMessage());
        }
        return "redirect:/pass"; // Redirige vers la page du pass
    }
}