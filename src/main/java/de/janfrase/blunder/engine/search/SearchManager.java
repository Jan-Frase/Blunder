/* Made by Jan Frase :) */
package de.janfrase.blunder.engine.search;

import de.janfrase.blunder.engine.backend.movegen.Move;
import de.janfrase.blunder.uci.UciMessageHandler;
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
        Thread.ofVirtual()
                .name("Search Thread")
                .start(
                        () -> {
                            int depth = 1;
                            do {
                                Move potentiallyAbortedMove = searcher.startSearching(depth);
                                depth++;

                                if (!searcher.stopSearchingImmediately.get()) {
                                    move.set(potentiallyAbortedMove);
                                    UciMessageHandler.getInstance()
                                            .sendInfo("depth", Integer.toString(depth - 1));
                                }
                            } while (!searcher.stopSearchingImmediately.get());
                            UciMessageHandler.getInstance().searchIsFinished(move.get().toString());
                        });

        if (searchLimitations.moveTime() != -1) {
            try {
                Thread.sleep(searchLimitations.moveTime());
                searcher.stopSearchingImmediately.set(true);
            } catch (InterruptedException e) {
                LOGGER.error("Thread sleep was interrupted", e);
            }
        }

        // The only reason why we return the move is to allow for easy unit testing without having
        // to parse the sysout stream.
        return move.get();
    }
}
