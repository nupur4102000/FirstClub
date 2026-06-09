package com.firstclub.membership.controller;

import java.util.List;

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
        return ResponseEntity.ok(service.getAllPlans());
    }

    @PostMapping("/subscribe")
    public ResponseEntity<UserSubscription> subscribe(@RequestParam String userId, 
                                                      @RequestParam String planId, 
                                                      @RequestBody UserContextDto context) {
        return ResponseEntity.ok(service.subscribe(userId, planId, context));
    }

    @PutMapping("/tier/sync")
    public ResponseEntity<UserSubscription> updateTier(@RequestParam String userId, 
                                                       @RequestBody UserContextDto context) {
        return ResponseEntity.ok(service.updateTier(userId, context));
    }

    @PostMapping("/cancel")
    public ResponseEntity<Void> cancel(@RequestParam String userId) {
        service.cancelSubscription(userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/track/{userId}")
    public ResponseEntity<UserSubscription> track(@PathVariable String userId) {
        return service.getActiveSubscription(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}