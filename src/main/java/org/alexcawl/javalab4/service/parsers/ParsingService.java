package org.alexcawl.javalab4.service.parsers;

import lombok.extern.slf4j.Slf4j;
import org.alexcawl.javalab4.model.ParsingElement;
import org.alexcawl.javalab4.model.SearchArea;
import org.alexcawl.javalab4.model.SearchType;
import org.alexcawl.javalab4.model.dto.parsers.ParsingResponse;
import org.alexcawl.javalab4.model.exception.InvalidLimitException;
import org.alexcawl.javalab4.model.exception.NoStrategyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ParsingService {
    @Autowired
    private List<ParsingByTypeStrategy> strategies;

    public ResponseEntity<ParsingResponse> parse(SearchArea area, SearchType type, String key, Integer limit) {
        try {
            validateLimit(limit);

            List<ParsingElement> elements = strategies.stream()
                    .filter(strategy -> strategy.getType().equals(type))
                    .findAny()
                    .orElseThrow(NoStrategyException::new)
                    .parse(area, key, limit);

            return new ResponseEntity<>(
                    new ParsingResponse(elements,200, "OK"),
                    HttpStatus.OK
            );
        } catch (NoStrategyException exception) {
            return new ResponseEntity<>(
                    new ParsingResponse(400, "Invalid strategy"),
                    HttpStatus.BAD_REQUEST
            );
        } catch (InvalidLimitException exception) {
            return new ResponseEntity<>(
                    new ParsingResponse(400, "Invalid limit"),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    private void validateLimit(Integer limit) throws InvalidLimitException {
        if (limit < 0) {
            throw new InvalidLimitException();
        }
    }
}
