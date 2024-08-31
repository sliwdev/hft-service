package org.example.hftservice.stat;

import io.micronaut.context.annotation.Factory;
import jakarta.inject.Singleton;

@Factory
class StatConfig {

    @Singleton
    StatService statService() {
        return new StatService();
    }

    static final int MIN_EXPONENT = 1;
    static final int MAX_EXPONENT = 8;
}
