/* Made by Jan Frase :) */
package de.janfrase.blunder.engine.backend.state.game;

import java.util.ArrayList;

public class RepeatTable {

    private final ArrayList<Long> zobristHashes = new ArrayList<>();
    private boolean isRepeat = false;

    boolean isRepeat() {
        return isRepeat;
    }

    void addHash(long hash) {
        isRepeat = zobristHashes.contains(hash);
        zobristHashes.add(hash);
    }

    void removeLastHash() {
        zobristHashes.removeLast();
    }
}
