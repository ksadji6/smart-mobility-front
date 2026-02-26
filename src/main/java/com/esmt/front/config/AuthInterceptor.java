package com.esmt.front.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) throws Exception {
        HttpSession session = req.getSession(false);

        // 1. Vérification de la connexion
        if (session == null || session.getAttribute("token") == null) {
            res.sendRedirect("/login?expired=true");
            return false;
        }

        // 2. Vérification du rôle pour les routes d'administration
        String uri = req.getRequestURI();
        String role = (String) session.getAttribute("role");

        if (uri.startsWith("/admin") && !"ADMIN".equals(role)) {
            // Si l'utilisateur n'est pas ADMIN, on le renvoie au dashboard avec un message d'erreur
            res.sendRedirect("/dashboard?error=access-denied");
            return false;
        }

        return true;
    }
}