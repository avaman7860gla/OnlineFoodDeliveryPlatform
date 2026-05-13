package com.example.delivery.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.delivery.entity.DeliveryAgent;
import com.example.delivery.service.DeliveryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/delivery")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService service;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody DeliveryAgent agent) {
        return ResponseEntity.ok(service.registerAgent(agent));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.getAgentById(id));
    }

    @GetMapping("/nearby")
    public ResponseEntity<?> getNearby(@RequestParam double lat,
                                       @RequestParam double lon) {
        return ResponseEntity.ok(service.getNearbyAgents(lat, lon));
    }

    @PutMapping("/location")
    public ResponseEntity<?> updateLocation(@RequestParam Integer agentId,
                                           @RequestParam double lat,
                                           @RequestParam double lon) {
        service.updateLocation(agentId, lat, lon);
        return ResponseEntity.ok("Location updated");
    }

    @PutMapping("/availability")
    public ResponseEntity<?> setAvailability(@RequestParam Integer agentId,
                                            @RequestParam boolean available) {
        service.setAvailability(agentId, available);
        return ResponseEntity.ok("Updated");
    }

    @PutMapping("/verify/{agentId}")
    public ResponseEntity<?> verify(@PathVariable Integer agentId) {
        service.verifyAgent(agentId);
        return ResponseEntity.ok("Verified");
    }

    @PostMapping("/assign")
    public ResponseEntity<?> assign(@RequestParam Integer agentId,
                                   @RequestParam Integer orderId) {
        service.assignOrder(agentId, orderId);
        return ResponseEntity.ok("Order assigned");
    }

    @PostMapping("/complete/{agentId}")
    public ResponseEntity<?> complete(@PathVariable Integer agentId, @RequestHeader("Authorization") String token) {
        service.completeDelivery(agentId, token);
        return ResponseEntity.ok("Delivery completed");
    }

    @PutMapping("/rating")
    public ResponseEntity<?> rating(@RequestParam Integer agentId,
                                   @RequestParam double rating) {
        service.updateRating(agentId, rating);
        return ResponseEntity.ok("Rating updated");
    }

    @GetMapping("/active/{agentId}")
    public ResponseEntity<?> active(@PathVariable Integer agentId) {
        return ResponseEntity.ok(service.getActiveDeliveries(agentId));
    }
}