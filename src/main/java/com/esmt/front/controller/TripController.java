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
@RequestMapping("/trips")
@RequiredArgsConstructor
public class TripController {

    private final ApiService apiService;

    @GetMapping
    public String tripsPage(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        String token = (String) session.getAttribute("token");

        model.addAttribute("trips", apiService.getTripHistory(userId, token));
        model.addAttribute("tripRequest", new TripRequest());
        model.addAttribute("activeMenu", "trips");
        return "trips/list";
    }

    @PostMapping("/new")
    public String newTrip(@ModelAttribute TripRequest request, HttpSession session, RedirectAttributes ra) {
        String token = (String) session.getAttribute("token");
        request.setUserId((Long) session.getAttribute("userId"));
        try {
            apiService.registerTrip(request, token);
            ra.addFlashAttribute("success", "Trajet enregistré !");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/trips";
    }
}