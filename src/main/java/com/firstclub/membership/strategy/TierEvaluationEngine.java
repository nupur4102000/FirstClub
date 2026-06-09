package com.firstclub.membership.strategy;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.firstclub.membership.dto.UserContextDto;
import com.firstclub.membership.model.TierLevel;

@Component
public class TierEvaluationEngine {
    private final List<TierEvaluationStrategy> strategies;

    // Dependency Inversion Principle (DIP): Injected list of polymorphic strategies
    public TierEvaluationEngine(List<TierEvaluationStrategy> strategies) {
        this.strategies = strategies;
    }

    public TierLevel determineBestTier(UserContextDto context) {
        TierLevel highest = TierLevel.SILVER;
        for (TierEvaluationStrategy strategy : strategies) {
            Optional<TierLevel> res = strategy.evaluate(context);
            if (res.isPresent() && res.get().ordinal() > highest.ordinal()) {
                highest = res.get();
            }
        }
        return highest;
    }
}