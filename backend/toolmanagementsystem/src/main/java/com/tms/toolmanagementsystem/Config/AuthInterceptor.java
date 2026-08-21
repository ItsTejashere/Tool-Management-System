package com.tms.toolmanagementsystem.Config;

import com.tms.toolmanagementsystem.util.JwtUtil;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 🚀 SECURITY FIX: Validates that non-OWNER users have assigned plantId & deptId
 * Prevents privilege escalation where INVENTORY users with NULL IDs get full access
 */
@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Extract JWT from Authorization header
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // Public endpoints - no validation needed
            return true;
        }

        try {
            String token = authHeader.substring(7);
            
            // Parse JWT claims
            var claims = Jwts.parser()
                    .setSigningKey(jwtSecret.getBytes())
                    .parseClaimsJws(token)
                    .getBody();

            String role = (String) claims.get("role");
            Object plantIdObj = claims.get("plantId");
            Object deptIdObj = claims.get("deptId");

            String plantIdStr = plantIdObj != null ? plantIdObj.toString() : "null";
            String deptIdStr = deptIdObj != null ? deptIdObj.toString() : "null";

            // 🚀 SECURITY CHECK: Non-OWNER users must have assigned plant & dept
            if (!"OWNER".equals(role)) {
                if ("null".equals(plantIdStr) || "null".equals(deptIdStr) || 
                    plantIdStr.isEmpty() || deptIdStr.isEmpty()) {
                    
                    // Log security violation
                    System.err.println("🚨 SECURITY VIOLATION: User with role '" + role + 
                                     "' has null plant_id/dept_id attempting access to: " + request.getRequestURI());
                    
                    response.setStatus(HttpStatus.FORBIDDEN.value());
                    response.setContentType("application/json");
                    response.getWriter().write("{\"status\": false, \"message\": \"Insufficient permissions: User not assigned to any facility or department.\"}");
                    return false;
                }
            }

            // Store user info in request attributes for controllers to use
            request.setAttribute("userRole", role);
            request.setAttribute("plantId", plantIdStr);
            request.setAttribute("deptId", deptIdStr);
            request.setAttribute("username", claims.getSubject());

            return true;

        } catch (Exception e) {
            System.err.println("🚨 JWT Parsing failed: " + e.getMessage());
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json");
            response.getWriter().write("{\"status\": false, \"message\": \"Invalid or expired token.\"}");
            return false;
        }
    }
}
