package org.example.hftservice.stat;

class Stat {

    private int k;
    private double min, max, last, avg, var;
    private Double secondMin, secondMax;

    private Stat() {
    }

    Stat(int k, double price) {
        this.k = k;
        this.min = price;
        this.max = price;
        this.last = price;
        this.avg = price;
        this.var = 0d;
    }


    void refresh(double oldFirst, int oldCount, double price) {
        min(price);
        max(price);
        last(price);
        avgAndVar(oldFirst, oldCount, price);
    }

    private void min(double price) {
        if (price < min) {
            secondMin = min;
            min = price;
        } else {
            if (secondMin == null || price < secondMin) {
                secondMin = price;
            }
        }
    }

    private void max(double price) {
        if (price > max) {
            secondMax = max;
            max = price;
        } else {
            if (secondMax == null || price > secondMax) {
                secondMax = price;
            }
        }
    }

    private void last(double price) {
        last = price;
    }

    private void avgAndVar(double oldSubFirst, int count, double newLast) {
        double pow = Math.pow(10, k);
        double oldAvg = avg;
        if (count < pow) {
            avg = ((avg * count) + newLast) / (count + 1);
            double sumOfSquaredDeviations = var * count
                    + Math.pow(newLast - oldAvg, 2)
                    + (Math.pow(oldAvg - avg, 2) * count) / (count + 1);
            var = sumOfSquaredDeviations / (count + 1);
        } else {
            avg = ((avg * pow) - oldSubFirst + newLast) / pow;
            double sumOfSquaredDeviations = var * pow
                    - Math.pow(oldSubFirst - oldAvg, 2)
                    + Math.pow(newLast - avg, 2);
            var = sumOfSquaredDeviations / pow;
        }
    }

    Stat copy() {
        Stat copy = new Stat();
        copy.k = this.k;
        copy.min = this.min;
        copy.max = this.max;
        copy.last = this.last;
        copy.avg = this.avg;
        copy.var = this.var;
        copy.secondMin = this.secondMin;
        copy.secondMax = this.secondMax;
        return copy;
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    public double getLast() {
        return last;
    }

    public double getAvg() {
        return avg;
    }

    public double getVar() {
        return var;
    }
}
