package org.alexcawl.javalab4.service.parsers;

import lombok.extern.slf4j.Slf4j;
import org.alexcawl.javalab4.dao.ContentRepository;
import org.alexcawl.javalab4.dao.Resource;
import org.alexcawl.javalab4.dao.ResourceRepository;
import org.alexcawl.javalab4.model.ParsingElement;
import org.alexcawl.javalab4.model.SearchArea;
import org.alexcawl.javalab4.model.SearchType;
import org.alexcawl.javalab4.model.exception.NoStrategyException;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ParsingUrlService implements ParsingByTypeStrategy {
    @Autowired
    List<ParsingByAreaStrategy> strategies;

    @Autowired
    private ContentRepository contentRepository;

    @Autowired
    private ResourceRepository resourceRepository;

    @Override
    public SearchType getType() {
        return SearchType.EXTERNAL_URL;
    }

    @Override
    public List<ParsingElement> parse(SearchArea area, String key, Integer limit) throws NoStrategyException {
        List<String> nodesID = strategies.stream()
                .filter(strategy -> strategy.getArea().equals(area))
                .findAny()
                .orElseThrow(NoStrategyException::new)
                .getContentNodes(key);

        List<ParsingElement> elements = new ArrayList<>();

        for (String nodeID: nodesID) {
            try {
                String content = contentRepository.findById(nodeID).orElseThrow().getContent();
                Resource resource = resourceRepository.findById(nodeID).orElseThrow();
                String domain = resource.getDomain();
                String url = resource.getURL();

                elements.addAll(Jsoup.parse(content).select("a[href]")
                        .stream()
                        .map(element -> element.attr("abs:href"))
                        .filter(element -> {
                            try {
                                URL baseUrl = new URL(element);
                                return !baseUrl.getHost().equals(domain);
                            } catch (Exception exception) {
                                return false;
                            }
                        })
                        .collect(Collectors.toSet())
                        .stream()
                        .map(element -> new ParsingElement(element, url))
                        .toList()
                );
            } catch (Exception exception) {
                log.warn(nodeID + " has no content");
            }
        }

        if (limit == 0) {
            return elements.stream().distinct().toList();
        } else {
            return elements.stream().distinct().limit(limit).toList();
        }
    }
}
