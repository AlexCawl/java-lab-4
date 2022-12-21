package org.alexcawl.javalab4.service.parsers;

import org.alexcawl.javalab4.model.SearchArea;
import org.alexcawl.javalab4.model.SearchType;

import java.util.List;

public interface ParsingByAreaStrategy {
    SearchArea getArea();
    List<String> getContentNodes(String key);
}
