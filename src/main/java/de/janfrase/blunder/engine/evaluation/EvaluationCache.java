package de.janfrase.blunder.engine.evaluation;

import java.util.function.Supplier;

public class EvaluationCache {

    public record HashAndEval(long hash, float eval) {
    }

    private final HashAndEval[] cache;

    public EvaluationCache(int size) {
        this.cache = new HashAndEval[size];
    }

    public float checkHash(long zobristHash, Supplier<Float> eval) {

        int index = Math.toIntExact(Math.abs(zobristHash) % this.cache.length);

        HashAndEval oldValue = this.cache[index];

        if (oldValue == null || oldValue.hash != zobristHash) {
            HashAndEval newValue = new HashAndEval(zobristHash, eval.get());
            this.cache[index] = newValue;
        }
        return cache[index].eval;
    }

}
