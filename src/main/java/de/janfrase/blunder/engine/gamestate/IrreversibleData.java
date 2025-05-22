package de.janfrase.blunder.engine.gamestate;

import java.util.OptionalInt;


public record IrreversibleData(CastlingRights castlingRights, OptionalInt enPassantX, int halfMoveClock) {
}
