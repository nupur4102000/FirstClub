package com.firstclub.membership.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.firstclub.membership.model.MembershipPlan;

@Repository
public interface MembershipPlanRepository extends JpaRepository<MembershipPlan, String> {}