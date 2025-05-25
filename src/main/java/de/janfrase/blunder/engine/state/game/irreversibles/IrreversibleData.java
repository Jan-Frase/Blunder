/* Made by Jan Frase :) */
package de.janfrase.blunder.engine.state.game.irreversibles;

import java.util.OptionalInt;

public record IrreversibleData(
        CastlingRights castlingRights, OptionalInt enPassantX, int halfMoveClock) {

    /**
     * A builder class for constructing instances of {@code IrreversibleData}.
     * The builder allows for the setting of specific components that comprise
     * an {@code IrreversibleData} instance in a controlled and stepwise manner.
     */
    public static class Builder {
        private CastlingRights castlingRights;
        private OptionalInt enPassantX;
        private int halfMoveClock;

        public Builder transferFromOldState(IrreversibleData irreversibleData) {
            this.castlingRights = irreversibleData.castlingRights();
            this.enPassantX = OptionalInt.empty();
            this.halfMoveClock = irreversibleData.halfMoveClock() + 1;
            return this;
        }

        public Builder castlingRights(CastlingRights castlingRights) {
            this.castlingRights = castlingRights;
            return this;
        }

        public Builder enPassantX(OptionalInt enPassantX) {
            this.enPassantX = enPassantX;
            return this;
        }

        public Builder halfMoveClock(int halfMoveClock) {
            this.halfMoveClock = halfMoveClock;
            return this;
        }

        public IrreversibleData build() {
            return new IrreversibleData(castlingRights, enPassantX, halfMoveClock);
        }

        public IrreversibleData buildDefault() {
            return new IrreversibleData(
                    new CastlingRights(true, true, true, true), OptionalInt.empty(), 0);
        }
    }
}
