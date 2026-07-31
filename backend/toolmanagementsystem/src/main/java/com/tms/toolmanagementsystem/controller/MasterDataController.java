package com.tms.toolmanagementsystem.controller;

import com.tms.toolmanagementsystem.entity.Machine;
import com.tms.toolmanagementsystem.entity.Project;
import com.tms.toolmanagementsystem.repository.MasterDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "${cors.allowed-origins}")
public class MasterDataController {

    @Autowired
    private MasterDataRepository masterDataRepository;

    @GetMapping("/machines")
    public ResponseEntity<List<Machine>> getMachines() {
        return ResponseEntity.ok(masterDataRepository.getAllMachines());
    }

    // GET endpoint to fetch projects for a specific department
    @GetMapping("/projects/{departmentId}")
    public ResponseEntity<List<Project>> getProjectsByDepartment(@PathVariable Integer departmentId) {
        List<Project> projects = masterDataRepository.getProjectsByDepartment(departmentId);
        return ResponseEntity.ok(projects);
    }

    // POST endpoint to add a project
    @PostMapping("/projects")
    public ResponseEntity<?> createProject(@RequestBody Project project) {
        boolean isAdded = masterDataRepository.addProject(project); // use your repository name
        if (isAdded) {
            return ResponseEntity.ok("{\"status\": true, \"message\": \"Project created successfully\"}");
        }
        return ResponseEntity.badRequest().body("{\"status\": false, \"message\": \"Failed to create project\"}");
    }

    // DELETE endpoint to remove a project
    @DeleteMapping("/projects/{projectId}")
    public ResponseEntity<?> deleteProject(@PathVariable Integer projectId) {
        boolean isDeleted = masterDataRepository.deleteProject(projectId); // use your repository name
        if (isDeleted) {
            return ResponseEntity.ok("{\"status\": true, \"message\": \"Project deleted successfully\"}");
        }
        return ResponseEntity.badRequest().body("{\"status\": false, \"message\": \"Cannot delete project. Tools might be assigned to it.\"}");
    }
}