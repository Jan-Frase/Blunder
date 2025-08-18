/* Made by Jan Frase :) */
package de.janfrase.blunder.engine.search;

import de.janfrase.blunder.engine.search.algos.NegaMax;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SearchManager {

    // logging
    private static final Logger LOGGER = LogManager.getLogger();

    // singleton pattern
    private static final SearchManager INSTANCE = new SearchManager();

    private SearchManager() {}

    public static SearchManager getInstance() {
        return INSTANCE;
    }

    public void go() {
        NegaMax.startSearching(4);
    }
}
