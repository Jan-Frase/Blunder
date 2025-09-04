/* Made by Jan Frase :) */
package de.janfrase.blunder.engine.search;

import de.janfrase.blunder.engine.backend.movegen.Move;
import de.janfrase.blunder.engine.backend.state.game.GameState;
import de.janfrase.blunder.uci.UciMessageHandler;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SearchManager {

    // logging
    private static final Logger LOGGER = LogManager.getLogger(SearchManager.class);

    // singleton pattern
    private static final SearchManager INSTANCE = new SearchManager();

    private SearchManager() {}

    public static SearchManager getInstance() {
        return INSTANCE;
    }

    public Move go(SearchLimitations searchLimitations) {
        Searcher searcher = new Searcher();
        AtomicReference<Move> move = new AtomicReference<>();

        startSearchThread(searcher, move);

        // TODO: Im not even starting a thread here lol!
        // This blocks the UCI thread and needs to be changed.
        startTimeoutThread(searcher, searchLimitations);

        // TODO: This cant return the move since we cant wait for it to be computed.
        // The only reason why we return the move is to allow for easy unit testing without having
        // to parse the sysout stream.
        return move.get();
    }

    private void startSearchThread(Searcher searcher, AtomicReference<Move> move) {
        Thread.ofVirtual().name("Search Thread").start(() -> iterativeDeepening(searcher, move));
    }

    private void iterativeDeepening(Searcher searcher, AtomicReference<Move> move) {
        int depth = 1;
        ArrayList<Move> previousPrincipalVariation = new ArrayList<>();
        do {
            SearchResult searchResult = searcher.startSearching(depth, previousPrincipalVariation);
            depth++;

            // if we properly finished this search
            if (!searcher.stopSearchingImmediately.get()) {

                move.set(searchResult.principalVariation().getFirst());
                // UCI info string
                StringBuilder sb = new StringBuilder();
                sb.append("depth ").append(depth - 1).append(" ");
                sb.append("score cp ")
                        .append(
                                (int)
                                        (GameState.getInstance().isWhitesTurn()
                                                ? searchResult.eval()
                                                : -searchResult.eval()))
                        .append(" ");
                sb.append("nodes ").append(searcher.getNodesSearched()).append(" ");
                sb.append("pv ");
                for (Move m : searchResult.principalVariation()) {
                    sb.append(m.toString()).append(" ");
                }

                previousPrincipalVariation = searchResult.principalVariation();
                UciMessageHandler.getInstance().sendInfo(sb.toString().trim());
            }
        } while (!searcher.stopSearchingImmediately.get());
        UciMessageHandler.getInstance().searchIsFinished(move.get().toString());
    }

    private void startTimeoutThread(Searcher searcher, SearchLimitations searchLimitations) {
        if (searchLimitations.moveTime() != -1) {
            try {
                Thread.sleep(searchLimitations.moveTime());
                searcher.stopSearchingImmediately.set(true);
            } catch (InterruptedException e) {
                LOGGER.error("Thread sleep was interrupted", e);
            }
        }
    }
}
