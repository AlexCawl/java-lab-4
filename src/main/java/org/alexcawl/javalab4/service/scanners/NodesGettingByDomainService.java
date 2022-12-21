package org.alexcawl.javalab4.service.scanners;

import lombok.extern.slf4j.Slf4j;
import org.alexcawl.javalab4.dao.Node;
import org.alexcawl.javalab4.dao.NodeRepository;
import org.alexcawl.javalab4.dao.NodeStatus;
import org.alexcawl.javalab4.model.SearchArea;
import org.alexcawl.javalab4.model.WebResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class NodesGettingByDomainService implements ScanningStrategy {
    @Autowired
    private NodeRepository nodeRepository;

    @Override
    public SearchArea getArea() {
        return SearchArea.DOMAIN;
    }

    @Override
    public List<WebResource> analyze(String key) {
        return nodeRepository.findAllByDomain(key)
                .stream()
                .map(element -> {
                    String url = element.getURL();
                    NodeStatus status = element.getNodeStatus();
                    Integer nesting = element.getNesting();

                    if (url.endsWith("/")) {
                        url = url.substring(0, url.length() - 1);
                    }

                    return new WebResource(url, status, nesting);
                })
                .collect(Collectors.toSet())
                .stream()
                .sorted(Comparator.comparing(WebResource::getNesting))
                .toList();
    }
}
