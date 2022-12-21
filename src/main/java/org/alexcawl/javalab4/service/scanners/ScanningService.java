package org.alexcawl.javalab4.service.scanners;

import lombok.extern.slf4j.Slf4j;
import org.alexcawl.javalab4.model.SearchArea;
import org.alexcawl.javalab4.model.WebResource;
import org.alexcawl.javalab4.model.dto.scanners.ScanningResponse;
import org.alexcawl.javalab4.model.dto.taskers.TaskingResponse;
import org.alexcawl.javalab4.model.exception.NoStrategyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ScanningService {
    @Autowired
    private List<ScanningStrategy> strategies;

    public ResponseEntity<ScanningResponse> analyze(SearchArea area, String key) {
        try {
            List<WebResource> resources = strategies.stream()
                    .filter(strategy -> strategy.getArea().equals(area))
                    .findAny()
                    .orElseThrow(NoStrategyException::new)
                    .analyze(key);
            ScanningResponse response = new ScanningResponse(resources, 200, "OK");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (NoStrategyException exception) {
            return new ResponseEntity<>(
                    new ScanningResponse(400, "Invalid strategy"),
                    HttpStatus.BAD_REQUEST
            );
        }

    }
}
