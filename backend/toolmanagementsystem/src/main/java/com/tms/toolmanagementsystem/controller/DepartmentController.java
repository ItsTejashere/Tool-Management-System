package com.tms.toolmanagementsystem.controller;

import com.tms.toolmanagementsystem.entity.Department;
import com.tms.toolmanagementsystem.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "${cors.allowed-origins}")
public class DepartmentController {

    @Autowired
    private DepartmentRepository departmentRepository;

    // 🚀 CACHE: Saves the department list in RAM based on the specific plant selected
    @Cacheable(value = "departments", key = "#plantId")
    @GetMapping("/departments/{plantId}")
    public ResponseEntity<List<Department>> getDepartmentsByPlant(@PathVariable Integer plantId) {
        List<Department> departments = departmentRepository.findByPlantId(plantId);
        return ResponseEntity.ok(departments);
    }
}
