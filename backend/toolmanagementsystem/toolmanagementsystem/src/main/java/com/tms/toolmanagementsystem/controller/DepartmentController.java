package com.tms.toolmanagementsystem.controller;

import com.tms.toolmanagementsystem.entity.Department;
import com.tms.toolmanagementsystem.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class DepartmentController {

    @Autowired
    private DepartmentRepository departmentRepository;

    // The {plantId} in the URL is extracted by the @PathVariable annotation
    @GetMapping("/departments/{plantId}")
    public ResponseEntity<List<Department>> getDepartmentsByPlant(@PathVariable Integer plantId) {
        List<Department> departments = departmentRepository.findByPlantId(plantId);
        return ResponseEntity.ok(departments);
    }
}