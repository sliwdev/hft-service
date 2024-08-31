package org.example.hftservice.stat

import spock.lang.Specification

import static org.example.hftservice.stat.Symbol.A

class StatServiceSpec extends Specification {

    StatService statService = new StatService()

    void 'should get stat'() {
        given:
        statService.addTrade(A, 1.23)

        when:
        def result = statService.getStat(A, 1).block()

        then:
        result.min == 1.23d
        result.max == 1.23d
        result.last == 1.23d
        result.avg == 1.23d
        result.var == 0
    }
}
