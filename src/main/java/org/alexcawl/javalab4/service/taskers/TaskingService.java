package org.alexcawl.javalab4.service.taskers;

import lombok.extern.slf4j.Slf4j;
import org.alexcawl.javalab4.dao.Task;
import org.alexcawl.javalab4.dao.TaskRepository;
import org.alexcawl.javalab4.model.dto.taskers.TaskingResponse;
import org.alexcawl.javalab4.model.exception.InvalidDepthException;
import org.alexcawl.javalab4.model.exception.InvalidUrlException;
import org.alexcawl.javalab4.model.exception.PageNotFoundException;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

@Slf4j
@Service
public class TaskingService {
    @Autowired
    private TaskRepository taskRepository;

    @Value("${configuration.crawler.depth}")
    private Integer maxDepth;

    public ResponseEntity<TaskingResponse> scan(String url, Integer depth) {
        try {
            validateUrl(url);
            validateDepth(depth);

            url = configureUrl(url);

            taskRepository.saveAndFlush(new Task(url, depth));
            return new ResponseEntity<>(
                    new TaskingResponse(200, "OK"),
                    HttpStatus.OK
            );
        } catch (InvalidUrlException exception) {
            return new ResponseEntity<>(
                    new TaskingResponse(400, "Invalid URL syntax"),
                    HttpStatus.BAD_REQUEST
            );
        } catch (PageNotFoundException exception) {
            return new ResponseEntity<>(
                    new TaskingResponse(404, "Page by URL not found"),
                    HttpStatus.NOT_FOUND
            );
        } catch (InvalidDepthException exception) {
            return new ResponseEntity<>(
                    new TaskingResponse(400, "Depth should be more than 0"),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    private void validateUrl(String url) throws InvalidUrlException, PageNotFoundException {
        try {
            new URI(url).toURL().toString();
        } catch (URISyntaxException | IllegalArgumentException | MalformedURLException exception) {
            throw new InvalidUrlException();
        }
    }

    private String configureUrl(String url) throws PageNotFoundException {
        try {
            return Jsoup.connect(url)
                    .followRedirects(false)
                    .userAgent("Mozilla/5.0 (X11; Linux x86_64; rv:105.0) Gecko/20100101 Firefox/105.0")
                    .get().baseUri();
        } catch (IOException exception) {
            throw new PageNotFoundException();
        }
    }

    private void validateDepth(Integer depth) throws InvalidDepthException {
        if (depth <= 0 || depth > maxDepth) {
            throw new InvalidDepthException();
        }
    }
}
