package com.firstclub.membership.strategy;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.firstclub.membership.dto.UserContextDto;
import com.firstclub.membership.model.TierLevel;

@Component
public class CohortStrategy implements TierEvaluationStrategy {
    @Override
    public Optional<TierLevel> evaluate(UserContextDto ctx) {
        if ("VIP_COHORT".equalsIgnoreCase(ctx.cohort())) return Optional.of(TierLevel.PLATINUM);
        return Optional.empty();
    }
}