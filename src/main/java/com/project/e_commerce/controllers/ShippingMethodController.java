package com.project.e_commerce.controllers;

import com.project.e_commerce.enums.ShippingMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${api.prefix}/shipping-methods")
public class ShippingMethodController {

    @GetMapping
    public ResponseEntity<?> getShippingMethods() {
        List<Map<String, Object>> methods = Arrays.stream(ShippingMethod.values())
            .map(method -> {
                Map<String, Object> map = new HashMap<>();
                map.put("code", method.name());
                map.put("description", method.getDescription());
                map.put("minDays", method.getMinDays());
                map.put("maxDays", method.getMaxDays());
                return map;
            })
            .collect(Collectors.toList());
        
        return ResponseEntity.ok(methods);
    }
} 