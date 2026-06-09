package com.firstclub.membership;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.firstclub.membership.model.MembershipPlan;
import com.firstclub.membership.model.PlanType;
import com.firstclub.membership.repository.MembershipPlanRepository;

@SpringBootApplication
public class MembershipApplication {
    public static void main(String[] args) {
        SpringApplication.run(MembershipApplication.class, args);
    }

    @Bean
    CommandLineRunner seedDatabase(MembershipPlanRepository repo) {
        return args -> {
            repo.save(new MembershipPlan("P-MON", PlanType.MONTHLY, 15.0));
            repo.save(new MembershipPlan("P-QTR", PlanType.QUARTERLY, 40.0));
            repo.save(new MembershipPlan("P-YR", PlanType.YEARLY, 120.0));
        };
    }
}