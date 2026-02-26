package com.esmt.front.service;

import com.esmt.front.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApiService {

    private final WebClient webClient;

    // --- AUTHENTIFICATION ---
    public AuthResponse login(LoginRequest request) {
        try {
            return webClient.post()
                    .uri("/api/auth/login")
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(AuthResponse.class)
                    .block();
        } catch (WebClientResponseException e) {
            log.error("Erreur login: {}", e.getResponseBodyAsString());
            throw new RuntimeException("Email ou mot de passe incorrect");
        }
    }

    public void register(RegisterRequest request) {
        try {
            webClient.post()
                    .uri("/api/users/register")
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(UserResponse.class)
                    .block();
        } catch (WebClientResponseException e) {
            throw new RuntimeException("Erreur lors de l'inscription : " + parseError(e));
        }
    }

    // --- UTILISATEUR & PASS ---
    public UserResponse getUser(Long userId, String token) {
        return webClient.get()
                .uri("/api/users/{id}", userId)
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToMono(UserResponse.class)
                .block();
    }

    public void suspendPass(String passNumber, String token) {
        webClient.put()
                .uri("/api/users/pass/{passNumber}/suspend", passNumber)
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    // --- TRAJETS (TRIPS) ---
    public TripResponse registerTrip(TripRequest request, String token) {
        return webClient.post()
                .uri("/api/trips/register")
                .header("Authorization", "Bearer " + token)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(TripResponse.class)
                .block();
    }

    public List<TripResponse> getTripHistory(Long userId, String token) {
        return webClient.get()
                .uri("/api/trips/history/{userId}", userId)
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToFlux(TripResponse.class)
                .collectList()
                .block();
    }

    // --- FACTURATION (BILLING) ---
    /*public BigDecimal getBalance(Long userId, String token) {
        try {
            return webClient.get()
                    .uri("/api/billing/balance/{userId}", userId)
                    .header("Authorization", "Bearer " + token)
                    .retrieve()
                    .bodyToMono(BigDecimal.class)
                    .block();
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }

    }*/

    /*public TxnResponse credit(CreditRequest request, String token) {
        try {
            return webClient.post()
                    .uri("/api/billing/recharge")
                    .header("Authorization", "Bearer " + token)
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(TxnResponse.class)
                    .block();
        } catch (WebClientResponseException e) {
            throw new RuntimeException("Échec de la recharge : " + parseError(e));
        }
    }*/

    public List<TransactionDTO> getTransactionHistory(Long userId, String token) {
        try {
            return webClient.get()
                    .uri("/api/billing/history/{userId}", userId)
                    .header("Authorization", "Bearer " + token)
                    .retrieve()
                    .bodyToFlux(TransactionDTO.class) 
                    .collectList()
                    .block();
        } catch (Exception e) {
            log.error("Erreur historique: {}", e.getMessage());
            return java.util.Collections.emptyList();
        }
    }

    // --- UTILITAIRE D'ERREUR ---
    private String parseError(WebClientResponseException e) {
        String body = e.getResponseBodyAsString();
        if (body.contains("message")) {
            // Extrait grossièrement le message JSON si présent
            return body.replaceAll(".*\"message\":\"([^\"]+)\".*", "$1");
        }
        return "Erreur serveur (Code: " + e.getStatusCode() + ")";
    }

    public List<UserResponse> getAllUsers(String token) {
        try {
            return webClient.get()
                    .uri("/api/users") // Vérifie que cette route existe dans ton User-Service
                    .header("Authorization", "Bearer " + token)
                    .retrieve()
                    .bodyToFlux(UserResponse.class)
                    .collectList()
                    .block();
        } catch (WebClientResponseException e) {
            log.error("Erreur getAllUsers: {}", e.getStatusCode());
            return List.of();
        }
    }



    // 1. REACTIVATION : Doit être un PUT vers le User-Service
    public void reactivatePass(String passNumber, String token) {
        webClient.put()
                .uri("/api/users/pass/{passNumber}/reactivate", passNumber)
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    // 2. SOLDE : Vérifie bien l'URL /api/billing/balance/
    public BigDecimal getBalance(Long userId, String token) {
        return webClient.get()
                .uri("/api/billing/balance/{userId}", userId)
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToMono(BigDecimal.class)
                .block();
    }

    // 3. RECHARGE : L'URL doit être /recharge (comme dans ton BillingController Backend)
    public void credit(CreditRequest request, String token) {
        webClient.post()
                .uri("/api/billing/recharge")
                .header("Authorization", "Bearer " + token)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}