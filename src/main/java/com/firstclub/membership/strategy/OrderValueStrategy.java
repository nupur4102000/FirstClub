package com.firstclub.membership.strategy;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.firstclub.membership.dto.UserContextDto;
import com.firstclub.membership.model.TierLevel;

@Component
public class OrderValueStrategy implements TierEvaluationStrategy {
    @Override
    public Optional<TierLevel> evaluate(UserContextDto ctx) {
        if (ctx.monthlyOrderValue() >= 5000) return Optional.of(TierLevel.PLATINUM);
        if (ctx.monthlyOrderValue() >= 2000) return Optional.of(TierLevel.GOLD);
        return Optional.of(TierLevel.SILVER);
    }
}