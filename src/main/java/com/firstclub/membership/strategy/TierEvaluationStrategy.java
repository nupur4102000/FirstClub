package com.firstclub.membership.strategy;

import java.util.Optional;

import com.firstclub.membership.dto.UserContextDto;
import com.firstclub.membership.model.TierLevel;

public interface TierEvaluationStrategy {
    Optional<TierLevel> evaluate(UserContextDto context);
}