package com.tms.toolmanagementsystem.controller;

import com.tms.toolmanagementsystem.entity.Plant;
import com.tms.toolmanagementsystem.repository.PlantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class PlantController {

    @Autowired
    private PlantRepository plantRepository;

    @GetMapping("/plants")
    public ResponseEntity<List<Plant>> getAllPlants() {
        List<Plant> plants = plantRepository.findAll();
        return ResponseEntity.ok(plants);
    }
}