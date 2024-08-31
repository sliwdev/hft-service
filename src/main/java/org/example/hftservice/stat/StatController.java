package org.example.hftservice.stat;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicReference;

import static com.fasterxml.jackson.core.JsonToken.FIELD_NAME;
import static com.fasterxml.jackson.core.JsonToken.VALUE_NUMBER_FLOAT;
import static org.example.hftservice.stat.StatConfig.MAX_EXPONENT;
import static org.example.hftservice.stat.StatConfig.MIN_EXPONENT;
import static org.example.hftservice.stat.Symbol.valueOf;


@Controller
class StatController {

    private final StatService statService;
    private final JsonFactory jsonFactory;

    StatController(StatService statService, JsonFactory jsonFactory) {
        this.statService = statService;
        this.jsonFactory = jsonFactory;
    }

    @Get("/stats")
    Mono<MutableHttpResponse<StatResponse>> getStat(@QueryValue Symbol symbol,
                                                    @QueryValue @Min(MIN_EXPONENT) @Max(MAX_EXPONENT) Integer k) {
        return statService.getStat(symbol, k)
                .map(stat -> new StatResponse(
                        stat.getMin(),
                        stat.getMax(),
                        stat.getLast(),
                        stat.getAvg(),
                        stat.getVar())
                ).map(HttpResponse::ok)
                .onErrorResume(e -> Mono.just(HttpResponse.notFound()));
    }

    @Post("/add_batch")
    Mono<HttpResponse<Void>> addBatch(@Body InputStream inputStream) {
        AtomicReference<Symbol> instrumentReference = new AtomicReference<>();

        Flux<Double> flux = Flux.create(sink -> {
            try (JsonParser parser = jsonFactory.createParser(inputStream)) {
                while (!parser.isClosed()) {
                    JsonToken token = parser.nextToken();

                    if (token == FIELD_NAME && "symbol".equals(parser.currentName())) {
                        instrumentReference.set(valueOf(parser.nextTextValue()));
                    }

                    if (token == VALUE_NUMBER_FLOAT) {
                        sink.next(parser.getDoubleValue());
                    }

                }
                sink.complete();
            } catch (IOException e) {
                sink.error(e);
            }
        });
        return flux
                .concatMap(price -> statService.addTrade(instrumentReference.get(), price))
                .then(Mono.just(HttpResponse.ok()));
        //todo onError
    }
}
