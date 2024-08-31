package org.example.hftservice.stat;

import java.util.ArrayList;
import java.util.List;

class TradingData {

    private final int minExponent;
    private final int maxExponent;

    TradingData(int minExponent, int maxExponent) {
        this.minExponent = minExponent;
        this.maxExponent = maxExponent;
    }

    private static class Node {
        final Double value;
        Node next;

        Node(Double value) {
            this.value = value;
        }
    }

    //we keep at most 1e8 prices per each symbol
    // which should take about 8GB memory full
    private Node first;
    private Node last;

    //we keep track of references to first nodes (and their following nodes)
    // in each of subsets of last 1e1, 1e2, ..., 1e8 trades
    private final List<Node> subFirsts = new ArrayList<>();
    private final List<Stat> stats = new ArrayList<>();

    private int count = 0;


    Stat getStat(Integer k) {
        return stats.get(k - 1).copy();
    }

    void addTrade(double price) {
        if (first == null) {
            first = new Node(price);
            last = first;
            for (int k = minExponent; k <= maxExponent; k++) {
                subFirsts.add(first);
                stats.add(new Stat(k, first.value));
            }
        } else {
            last.next = new Node(price);
            last = last.next;
            for (int k = minExponent; k <= maxExponent; k++) {
                Node subFirst = subFirsts.get(k - 1);
                stats.get(k - 1).refresh(subFirst.value, count, price);
                if (count >= Math.pow(10, k)) {
                    subFirsts.set(k - 1, subFirst.next);
                }
            }
        }
        count++;
        //trim leading trades if 1e8 reached
        if (count > 1e8) {
            first = first.next;
        }
    }
}