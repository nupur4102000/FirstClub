package com.firstclub.membership.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "membership_plans")
public class MembershipPlan {
    @Id
    private String id;
    
    @Enumerated(EnumType.STRING)
    private PlanType planType;
    
    private double price;

    public MembershipPlan() {}
    public MembershipPlan(String id, PlanType planType, double price) {
        this.id = id; 
        this.planType = planType; 
        this.price = price;
    }
    
    public String getId() { return id; }
    public PlanType getPlanType() { return planType; }
    public double getPrice() { return price; }
}