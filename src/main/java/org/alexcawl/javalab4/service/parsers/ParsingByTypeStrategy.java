package org.alexcawl.javalab4.service.parsers;

import org.alexcawl.javalab4.model.ParsingElement;
import org.alexcawl.javalab4.model.SearchArea;
import org.alexcawl.javalab4.model.SearchType;
import org.alexcawl.javalab4.model.exception.NoStrategyException;

import java.util.List;

public interface ParsingByTypeStrategy {
    SearchType getType();
    List<ParsingElement> parse(SearchArea area, String key, Integer limit) throws NoStrategyException;
}
