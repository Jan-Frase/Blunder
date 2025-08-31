/* Made by Jan Frase :) */
package de.janfrase.blunder.uci;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import org.junit.jupiter.api.Test;

class UciMessageHandlerTest {

    @Test
    void startParsing() {
        UciMessageHandler uciMessageHandler = UciMessageHandler.getInstance();

        String testCommand =
                "uci\n"
                    + "isready\n"
                    + "ucinewgame\n"
                    + "position fen r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq"
                    + " - 0 1\n"
                    + "go movetime 3000\n"
                    + "quit\n";
        InputStream inputStream = new ByteArrayInputStream(testCommand.getBytes());

        // 1. With Logging: 510k Nodes Depth 3
        // 2. Without logging in make and unmake move: 2.8M Nodes Depth 4

        uciMessageHandler.startParsing(inputStream);
    }
}
