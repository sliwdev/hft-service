package org.example.hftservice.stat

import spock.lang.Specification

class TradingDataSpec extends Specification {
    def "should get stat"() {
        given:
        def data = new TradingData(1, 2)
        data.addTrade(1.23)

        when:
        def result = data.getStat(1)

        then:
        result.min == 1.23d
        result.max == 1.23d
        result.last == 1.23d
        result.avg == 1.23d
        result.var == 0
    }

    def "should add trade"() {
        given:
        def data = new TradingData(1, 2)

        when:
        data.addTrade(1.23)

        then:
        data.first.value == 1.23
    }
}
