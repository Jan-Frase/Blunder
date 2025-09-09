package de.janfrase.blunder.engine.evaluation;

import java.util.function.Supplier;

public class EvaluationCash {

    private int numberOfCashHits = 0;
    private int numberOfCashMisses = 0;

    public record HashAndEval(long hash, float eval) {
    }

    private final HashAndEval[] cash;

    public EvaluationCash(int size) {
        this.cash = new HashAndEval[size];

    }

    public float checkHash(long zobristHash, Supplier<Float> eval) {

        int index = Math.toIntExact(Math.abs(zobristHash) % this.cash.length);

        HashAndEval oldValue = this.cash[index];

        if (oldValue == null || oldValue.hash != zobristHash) {
            HashAndEval newValue = new HashAndEval(zobristHash, eval.get());
            this.cash[index] = newValue;
            this.numberOfCashMisses++;
        } else {
            this.numberOfCashHits++;
        }
//        System.out.println("Cash hits: " + numberOfCashHits + ", cash misses: " + numberOfCashMisses +
//                ", percentage: " + numberOfCashHits / (float) (numberOfCashHits + numberOfCashMisses) * 100);
        return cash[index].eval;
    }

}
