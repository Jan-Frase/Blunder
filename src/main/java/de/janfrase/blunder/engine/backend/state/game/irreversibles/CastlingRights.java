/* Made by Jan Frase :) */
package de.janfrase.blunder.engine.backend.state.game.irreversibles;

import de.janfrase.blunder.engine.backend.Piece;

public record CastlingRights(
        boolean whiteLongCastle,
        boolean whiteShortCastle,
        boolean blackLongCastle,
        boolean blackShortCastle) {

    public boolean getLongCastle(Piece side) {
        if (side.getSide() == Piece.WHITE) {
            return this.whiteLongCastle;
        } else {
            return this.blackLongCastle;
        }
    }

    public boolean getShortCastle(Piece side) {
        if (side.getSide() == Piece.WHITE) {
            return this.whiteShortCastle;
        } else {
            return this.blackShortCastle;
        }
    }

    public static class Builder {
        boolean whiteLongCastle, whiteShortCastle, blackLongCastle, blackShortCastle;

        public Builder transferFromOldState(CastlingRights castlingRights) {
            this.whiteLongCastle = castlingRights.whiteLongCastle();
            this.whiteShortCastle = castlingRights.whiteShortCastle();
            this.blackLongCastle = castlingRights.blackLongCastle();
            this.blackShortCastle = castlingRights.blackShortCastle();

            return this;
        }

        public Builder whiteLongCastle(boolean whiteLongCastle) {
            this.whiteLongCastle = whiteLongCastle;
            return this;
        }

        public Builder whiteShortCastle(boolean whiteShortCastle) {
            this.whiteShortCastle = whiteShortCastle;
            return this;
        }

        public Builder blackLongCastle(boolean blackLongCastle) {
            this.blackLongCastle = blackLongCastle;
            return this;
        }

        public Builder blackShortCastle(boolean blackShortCastle) {
            this.blackShortCastle = blackShortCastle;
            return this;
        }

        public Builder disableSpecifiedCastle(
                boolean sideToDisabledRightsFor, boolean shortCastle) {
            if (sideToDisabledRightsFor) {
                if (shortCastle) {
                    this.whiteShortCastle = false;
                } else {
                    this.whiteLongCastle = false;
                }
            } else {
                if (shortCastle) {
                    this.blackShortCastle = false;
                } else {
                    this.blackLongCastle = false;
                }
            }
            return this;
        }

        public CastlingRights build() {
            return new CastlingRights(
                    whiteLongCastle, whiteShortCastle, blackLongCastle, blackShortCastle);
        }
    }
}
