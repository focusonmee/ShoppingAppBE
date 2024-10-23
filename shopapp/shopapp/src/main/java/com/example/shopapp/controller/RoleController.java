package com.example.shopapp.controller;

import com.example.shopapp.model.Role;
import com.example.shopapp.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;  // Import thư viện logger
import org.slf4j.LoggerFactory; // Import thư viện logger
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/roles") // Sửa lại đường dẫn cho đúng
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;

    // Khởi tạo Logger
    private static final Logger logger = LoggerFactory.getLogger(RoleController.class);

    @GetMapping("")
    public ResponseEntity<?> getAllRoles() {
        try {
            List<Role> roles = roleService.getAllRoles();
            return ResponseEntity.ok(roles);
        } catch (Exception e) {
            // Ghi lại lỗi
            logger.error("Error fetching roles: ", e);
            return ResponseEntity.status(500).body("An error occurred while fetching roles.");
        }
    }
}
