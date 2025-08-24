/* Made by Jan Frase :) */
package de.janfrase.blunder.engine.search;

import de.janfrase.blunder.engine.backend.movegen.move.Move;
import de.janfrase.blunder.engine.search.algos.Searcher;
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

    public void go() {

        Searcher searcher = new Searcher();

        Thread.ofVirtual()
                .name("Search Thread")
                .start(
                        () -> {
                            // TODO: Implement iterative deepening.
                            Move move = searcher.startSearching(4);
                            UciMessageHandler.getInstance().searchIsFinished(move.toString());
                        });
    }
}
