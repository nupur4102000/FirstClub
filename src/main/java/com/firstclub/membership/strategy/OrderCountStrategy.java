package com.firstclub.membership.strategy;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.firstclub.membership.dto.UserContextDto;
import com.firstclub.membership.model.TierLevel;

@Component
public class OrderCountStrategy implements TierEvaluationStrategy {
    @Override
    public Optional<TierLevel> evaluate(UserContextDto ctx) {
        if (ctx.monthlyOrderCount() >= 20) return Optional.of(TierLevel.PLATINUM);
        if (ctx.monthlyOrderCount() >= 10) return Optional.of(TierLevel.GOLD);
        return Optional.of(TierLevel.SILVER);
    } }