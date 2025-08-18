/* Made by Jan Frase :) */
package de.janfrase.blunder;

import de.janfrase.blunder.uci.UciMessageHandler;

public class Main {
    public static void main(String[] args) {
        UciMessageHandler.getInstance().startParsing();
    }
}
