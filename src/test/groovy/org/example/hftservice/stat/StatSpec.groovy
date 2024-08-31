package org.example.hftservice.stat

import spock.lang.Specification

import static spock.util.matcher.HamcrestMatchers.closeTo
import static spock.util.matcher.HamcrestSupport.that

class StatSpec extends Specification {

    def 'should refresh'() {
        given:
        def stat = new Stat(1, 1.23)

        when:
        stat.refresh(1.23, 1, 4.56)

        then:
        stat.min == 1.23d
        stat.max == 4.56d
        stat.last == 4.56d
        that stat.avg, closeTo(2.895d, 0.001d)
        that stat.var, closeTo(6.237d, 0.001d)
    }

    def 'should copy'() {
        given:
        def stat = new Stat(1, 1.23)

        when:
        def result = stat.copy()

        then:
        result.min == 1.23d
        result.max == 1.23d
        result.last == 1.23d
        result.avg == 1.23d
        result.var == 0d
    }
}
