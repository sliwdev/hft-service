package org.example.hftservice.stat;

import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.example.hftservice.stat.StatConfig.MAX_EXPONENT;
import static org.example.hftservice.stat.StatConfig.MIN_EXPONENT;

class StatService {

    Map<Symbol, TradingData> data = new ConcurrentHashMap<>();

    Mono<Stat> getStat(Symbol symbol, Integer k) {
        return Mono.fromCallable(() -> {
            TradingData tradingData = data.get(symbol);

            if (tradingData == null) {
                throw new RuntimeException("Data not found for symbol: " + symbol);
            }

            return tradingData.getStat(k);
        });
    }

    Mono<Void> addTrade(Symbol symbol, Double price) {
        TradingData tradingData = data.get(symbol);
        if (tradingData == null) {
            tradingData = new TradingData(MIN_EXPONENT, MAX_EXPONENT);
            tradingData.addTrade(price);
            data.put(symbol, tradingData);
        } else {
            tradingData.addTrade(price);
        }
        return Mono.empty();
    }

}
