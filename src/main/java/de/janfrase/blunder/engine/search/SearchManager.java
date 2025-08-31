/* Made by Jan Frase :) */
package de.janfrase.blunder.engine.search;

import de.janfrase.blunder.engine.backend.movegen.Move;
import de.janfrase.blunder.uci.UciMessageHandler;
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

    public void go(SearchLimitations searchLimitations) {
        Searcher searcher = new Searcher();
        Thread.ofVirtual()
                .name("Search Thread")
                .start(
                        () -> {
                            Move move = null;
                            int depth = 1;
                            do {
                                Move potentiallyAbortedMove = searcher.startSearching(depth);
                                depth++;

                                if (!searcher.stopSearchingImmediately.get()) {
                                    move = potentiallyAbortedMove;
                                    UciMessageHandler.getInstance()
                                            .sendInfo("depth", Integer.toString(depth - 1));
                                }
                            } while (!searcher.stopSearchingImmediately.get());
                            UciMessageHandler.getInstance().searchIsFinished(move.toString());
                        });

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
