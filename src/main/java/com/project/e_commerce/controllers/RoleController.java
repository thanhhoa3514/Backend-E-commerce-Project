package com.project.e_commerce.controllers;


import com.project.e_commerce.models.Role;
import com.project.e_commerce.responses.ResponseObject;
import com.project.e_commerce.services.role.IRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/admin/roles")
@RequiredArgsConstructor
@CrossOrigin("*")
public class RoleController {
    private final IRoleService roleService;
    @GetMapping("")
    public ResponseEntity<ResponseObject> getAllRoles() {
        List<Role> roles = roleService.getAllRoles();
        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Get roles successfully")
                .status(HttpStatus.OK)
                .data(roles)
                .build());
    }
}
