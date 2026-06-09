package com.firstclub.membership.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.firstclub.membership.dto.UserContextDto;
import com.firstclub.membership.model.MembershipPlan;
import com.firstclub.membership.model.UserSubscription;
import com.firstclub.membership.service.MembershipService;

@RestController
@RequestMapping("/api/v1/membership")
public class MembershipController {

    private final MembershipService service;

    public MembershipController(MembershipService service) { 
        this.service = service; 
    }

    @GetMapping("/plans")
    public ResponseEntity<List<MembershipPlan>> getPlans() {
        try {
            List<MembershipPlan> plans = service.getAllPlans();
            return ResponseEntity.ok(plans);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/subscribe")
    public ResponseEntity<Object> subscribe(@RequestParam String userId, 
                                            @RequestParam String planId, 
                                            @RequestBody UserContextDto context) {
        try {
            UserSubscription sub = service.subscribe(userId, planId, context);
            return ResponseEntity.ok(sub);
        } catch (Exception e) {
            // Catches "Active subscription already exists" or missing plans
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/tier/sync")
    public ResponseEntity<Object> updateTier(@RequestParam String userId, 
                                             @RequestBody UserContextDto context) {
        try {
            UserSubscription updatedSub = service.updateTier(userId, context);
            return ResponseEntity.ok(updatedSub);
        } catch (Exception e) {
            // Catches "No active subscription for user" and returns a 404 Not Found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/cancel")
    public ResponseEntity<Object> cancel(@RequestParam String userId) {
        try {
            service.cancelSubscription(userId);
            return ResponseEntity.ok("Subscription cancelled successfully.");
        } catch (Exception e) {
            // Catches "No active subscription found" and returns a 404 instead of a server crash
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/track/{userId}")
    public ResponseEntity<UserSubscription> track(@PathVariable String userId) {
        return service.getActiveSubscription(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}