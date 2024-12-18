package com.example.tp7.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpSession;

@Component
public class SessionInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);

        // Check if session exists and has the "chefprojet" attribute
        if (session == null || session.getAttribute("chefprojet") == null) {
            response.sendRedirect("/ChefProjet/login"); // Redirect to login if no session
            return false;
        }

        return true; // Allow request to proceed
    }
}
