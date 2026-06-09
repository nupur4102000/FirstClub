package com.firstclub.membership.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "subscriptions")
public class UserSubscription {
    @Id
    private String subscriptionId;
    private String userId;

    @ManyToOne
    @JoinColumn(name = "plan_id")
    private MembershipPlan plan;

    @Enumerated(EnumType.STRING)
    private TierLevel tierLevel;

    @Enumerated(EnumType.STRING)
    private SubscriptionStatus status;
    
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public UserSubscription() {}
    public UserSubscription(String subscriptionId, String userId, MembershipPlan plan, TierLevel tierLevel) {
        this.subscriptionId = subscriptionId;
        this.userId = userId;
        this.plan = plan;
        this.tierLevel = tierLevel;
        this.startDate = LocalDateTime.now();
        this.status = SubscriptionStatus.ACTIVE;
        
        int monthsToAdd = switch (plan.getPlanType()) {
            case MONTHLY -> 1;
            case QUARTERLY -> 3;
            case YEARLY -> 12;
        };
        this.endDate = this.startDate.plusMonths(monthsToAdd);
    }

    public String getSubscriptionId() { return subscriptionId; }
    public String getUserId() { return userId; }
    public MembershipPlan getPlan() { return plan; }
    public TierLevel getTierLevel() { return tierLevel; }
    public void setTierLevel(TierLevel tierLevel) { this.tierLevel = tierLevel; }
    public SubscriptionStatus getStatus() { return status; }
    public void setStatus(SubscriptionStatus status) { this.status = status; }
    public LocalDateTime getEndDate() { return endDate; }
}