/* Made by Jan Frase :) */
package de.janfrase.blunder.engine.backend.state.board;

public class BitBoard {
    public long value = 0;

    public BitBoard() {}

    public BitBoard(long value) {
        this.value = value;
    }

    public boolean getBit(int x, int y) {
        return (value & getLongWithBitAtIndex(x, y)) != 0;
    }

    public void setBit(int x, int y) {
        value |= getLongWithBitAtIndex(x, y);
    }

    public void clearBit(int x, int y) {
        value &= ~getLongWithBitAtIndex(x, y);
    }

    public static long getLongWithBitAtIndex(int x, int y) {
        int index = calculateIndex(x, y);
        return 1L << index;
    }

    public static int calculateIndex(int x, int y) {
        // The idea of this is that the most valuable bit is at the top left.
        // The top left - so a8 - is x=0 y=0.
        return 64 - (x + y * 8);
    }
}
