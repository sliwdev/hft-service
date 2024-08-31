package org.example.hftservice.stat;

import io.micronaut.core.annotation.Introspected;

@Introspected
public record StatResponse(Double min, Double max, Double last, Double avg, Double var) {
}
