package org.alexcawl.javalab4.service.parsers;

import lombok.extern.slf4j.Slf4j;
import org.alexcawl.javalab4.dao.NodeRepository;
import org.alexcawl.javalab4.dao.Resource;
import org.alexcawl.javalab4.dao.ResourceRepository;
import org.alexcawl.javalab4.model.SearchArea;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ResourcesGettingByUrlService implements ParsingByAreaStrategy {
    @Autowired
    private ResourceRepository resourceRepository;

    @Override
    public SearchArea getArea() {
        return SearchArea.URL;
    }

    @Override
    public List<String> getContentNodes(String key) {
        return resourceRepository.findAllByURLStartingWith(key)
                .stream()
                .map(Resource::getID)
                .toList();
    }
}
