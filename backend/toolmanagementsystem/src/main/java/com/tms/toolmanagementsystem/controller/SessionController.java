package com.tms.toolmanagementsystem.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/session")
@CrossOrigin(origins = "${cors.allowed-origins}")
public class SessionController {

    @GetMapping("/validate")
    public ResponseEntity<?> validateSession() {
        return ResponseEntity.ok(Map.of("status", true));
    }
}