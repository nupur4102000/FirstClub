package com.firstclub.membership.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.firstclub.membership.model.SubscriptionStatus;
import com.firstclub.membership.model.UserSubscription;

@Repository
public interface SubscriptionRepository extends JpaRepository<UserSubscription, String> {
    Optional<UserSubscription> findByUserIdAndStatus(String userId, SubscriptionStatus status);
}