package com.firstclub.membership.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.firstclub.membership.dto.UserContextDto;
import com.firstclub.membership.model.MembershipPlan;
import com.firstclub.membership.model.SubscriptionStatus;
import com.firstclub.membership.model.TierLevel;
import com.firstclub.membership.model.UserSubscription;
import com.firstclub.membership.repository.MembershipPlanRepository;
import com.firstclub.membership.repository.SubscriptionRepository;
import com.firstclub.membership.strategy.TierEvaluationEngine;

@Service
public class MembershipService {

    private final MembershipPlanRepository planRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final TierEvaluationEngine evaluationEngine;
    
    private final ConcurrentHashMap<String, ReentrantLock> userLocks = new ConcurrentHashMap<>();
    private final AtomicLong idGen = new AtomicLong(2000);

    public MembershipService(MembershipPlanRepository planRepository, 
                             SubscriptionRepository subscriptionRepository, 
                             TierEvaluationEngine evaluationEngine) {
        this.planRepository = planRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.evaluationEngine = evaluationEngine;
    }

    public List<MembershipPlan> getAllPlans() { 
        return planRepository.findAll(); 
    }

    private ReentrantLock getUserLock(String userId) {
        return userLocks.computeIfAbsent(userId, k -> new ReentrantLock());
    }

    @Transactional
    public UserSubscription subscribe(String userId, String planId, UserContextDto context) {
        ReentrantLock lock = getUserLock(userId);
        lock.lock();
        try {
            subscriptionRepository.findByUserIdAndStatus(userId, SubscriptionStatus.ACTIVE)
                .ifPresent(s -> { throw new IllegalStateException("Active subscription already exists"); });

            MembershipPlan plan = planRepository.findById(planId)
                .orElseThrow(() -> new IllegalArgumentException("Plan missing: " + planId));

            TierLevel finalTier = evaluationEngine.determineBestTier(context);
            UserSubscription sub = new UserSubscription("SUB-" + idGen.incrementAndGet(), userId, plan, finalTier);
            return subscriptionRepository.save(sub);
        } finally {
            lock.unlock();
        }
    }

    @Transactional
    public UserSubscription updateTier(String userId, UserContextDto context) {
        ReentrantLock lock = getUserLock(userId);
        lock.lock();
        try {
            UserSubscription sub = subscriptionRepository.findByUserIdAndStatus(userId, SubscriptionStatus.ACTIVE)
                .orElseThrow(() -> new NoSuchElementException("No active subscription for user"));

            TierLevel newTier = evaluationEngine.determineBestTier(context);
            sub.setTierLevel(newTier);
            return subscriptionRepository.save(sub);
        } finally {
            lock.unlock();
        }
    }

    @Transactional
    public void cancelSubscription(String userId) {
        ReentrantLock lock = getUserLock(userId);
        lock.lock();
        try {
            UserSubscription sub = subscriptionRepository.findByUserIdAndStatus(userId, SubscriptionStatus.ACTIVE)
                .orElseThrow(() -> new NoSuchElementException("No active subscription found"));
            sub.setStatus(SubscriptionStatus.CANCELLED);
            subscriptionRepository.save(sub);
        } finally {
            lock.unlock();
        }
    }

    public Optional<UserSubscription> getActiveSubscription(String userId) {
        return subscriptionRepository.findByUserIdAndStatus(userId, SubscriptionStatus.ACTIVE);
    }
}