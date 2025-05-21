package de.janfrase.core.gamestate;

import java.util.OptionalInt;


public record IrreversibleData(CastlingRights castlingRights, OptionalInt enPassantX, int halfMoveClock) {
}
