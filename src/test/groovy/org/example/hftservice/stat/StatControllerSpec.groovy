package org.example.hftservice.stat


import io.micronaut.http.HttpStatus
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

import static org.example.hftservice.stat.Symbol.A

@MicronautTest
class StatControllerSpec extends Specification {

    @Inject
    StatService statService

    @Inject
    StatController statController

    def 'should get stat'() {
        given:
        statService.addTrade(A, 1.23)

        when:
        def result = statController.getStat(A, 1).block()

        then:
        result.status == HttpStatus.OK
        result.body().min() == 1.23
    }


    def 'should add batch'() {
        def inputStream = new ByteArrayInputStream("""{
           "symbol": "A",
           "values": [1.23]
         }""".bytes)
        given:

        when:
        def result = statController.addBatch(inputStream).block()

        then:
        result.status == HttpStatus.OK
    }
}
