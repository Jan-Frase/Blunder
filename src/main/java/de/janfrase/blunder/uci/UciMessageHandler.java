/* Made by Jan Frase :) */
package de.janfrase.blunder.uci;

import de.janfrase.blunder.engine.backend.movegen.MoveGenerator;
import de.janfrase.blunder.engine.backend.movegen.move.Move;
import de.janfrase.blunder.engine.backend.state.game.FenParser;
import de.janfrase.blunder.engine.backend.state.game.GameState;
import de.janfrase.blunder.engine.search.SearchManager;
import java.util.Arrays;
import java.util.Scanner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UciMessageHandler {

    // singleton pattern
    private static final UciMessageHandler INSTANCE = new UciMessageHandler();

    // logging
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String IN = "In: ";
    private static final String OUT = "Out: ";

    private static class IncomingMessage {
        // incoming commands
        private static final String UCI = "uci";
        private static final String DEBUG = "debug";
        private static final String IS_READY = "isready";
        private static final String SET_OPTION = "setoption";
        private static final String REGISTER = "register";
        private static final String UCI_NEW_GAME = "ucinewgame";
        private static final String POSITION = "position";
        private static final String GO = "go";
        private static final String STOP = "stop";
        private static final String PONDERHIT = "ponderhit";
        private static final String QUIT = "quit";
    }

    private static class OutgoingMessage {
        // outgoing messages
        private static final String ID = "id Blunder";
        private static final String UCI_OK = "uciok";
        private static final String READY_OK = "readyok";
        private static final String BEST_MOVE = "bestmove";
        private static final String COPYPROTECTION = "copyprotection";
        private static final String REGISTRATION = "registration";
        private static final String INFO = "info";
        private static final String OPTION = "option";
    }

    private UciMessageHandler() {}

    public static UciMessageHandler getInstance() {
        return INSTANCE;
    }

    public void startParsing() {
        Thread.currentThread().setName("UCI Parser Thread");
        parse();
    }

    private void parse() {
        Scanner scanner = new Scanner(System.in);

        boolean quit = false;

        while (!quit) {
            // blocks until next line is found
            while (!scanner.hasNextLine()) {}
            String input = scanner.nextLine();

            LOGGER.info(IN + input);

            String[] tokens = input.split(" ");
            String commandName = tokens[0];

            String[] arguments = Arrays.copyOfRange(tokens, 1, tokens.length);

            switch (commandName) {
                case IncomingMessage.UCI -> uci();
                case IncomingMessage.QUIT -> quit = true;
                case IncomingMessage.UCI_NEW_GAME -> uciNewGame();
                case IncomingMessage.POSITION -> position(arguments);
                case IncomingMessage.GO -> go(arguments);
                case IncomingMessage.STOP -> stop();
                case IncomingMessage.IS_READY -> isReady();
            }
        }
    }

    private void isReady() {
        sendReply(OutgoingMessage.READY_OK);
    }

    private void sendReply(String message) {
        System.out.println(message);
        LOGGER.info(OUT + message);
    }

    private void uci() {
        sendReply(OutgoingMessage.ID);
        sendReply(OutgoingMessage.UCI_OK);
    }

    private void uciNewGame() {
        GameState.resetGameState();
        sendReply(OutgoingMessage.READY_OK);
    }

    private void position(String[] arguments) {
        // search for "moves" or end of string
        int indexOfMovesKeyword = arguments.length;
        for (int i = 0; i < arguments.length; i++) {
            String string = arguments[i];
            if (string.equals("moves")) {
                indexOfMovesKeyword = i;
                break;
            }
        }

        StringBuilder fenStringBuilder = new StringBuilder();
        for (int i = 0; i < indexOfMovesKeyword; i++) {
            fenStringBuilder.append(arguments[i]).append(" ");
        }
        String fenString = fenStringBuilder.toString().strip();

        if (fenString.equals("startpos")) {
            FenParser.loadStartingPosition();
        } else {
            FenParser.loadFenString(fenString);
        }

        for (int i = indexOfMovesKeyword + 1; i < arguments.length; i++) {
            String moveString = arguments[i];
            Move move = UciMoveParser.parseUciMove(moveString);
            GameState.getInstance().makeMove(move);
        }

        LOGGER.info("finished position command");
    }

    private void go(String[] arguments) {

        int depth = 0;

        for (int i = 1; i < arguments.length; i++) {
            String string = arguments[i];

            switch (string) {
                case "depth" -> {
                    depth = Integer.parseInt(arguments[i + 1]);
                    i++;
                }
            }
        }

        SearchManager.getInstance().go();
    }

    public void searchIsFinished(String move) {
        sendReply(OutgoingMessage.BEST_MOVE + " " + move);
    }

    private void stop() {
        Move move = MoveGenerator.generateLegalMoves().getFirst();
        sendReply(OutgoingMessage.BEST_MOVE + " " + move.toString());
    }
}
