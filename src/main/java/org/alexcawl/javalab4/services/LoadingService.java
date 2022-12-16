package org.alexcawl.javalab4.services;


import org.alexcawl.javalab4.dao.Task;
import org.alexcawl.javalab4.dao.TaskRepository;
import org.alexcawl.javalab4.models.exceptions.TaskValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

@Service
public class LoadingService {
    @Autowired
    private TaskRepository taskRepository;

    @Value("${resources.maxdepth}")
    private Integer maxDepth;

    public void sendTask(String url, Integer depth) throws TaskValidationException {
        if (isValidURL(url) && isValidDepth(depth)) {
            Task task = new Task(url, depth);
            taskRepository.save(task);
        } else {
            throw new TaskValidationException();
        }
    }

    private boolean isValidURL(String url) {
        try {
            new URL(url).toURI();
            return true;
        } catch (MalformedURLException | URISyntaxException e) {
            return false;
        }
    }

    private boolean isValidDepth(Integer depth) {
        return depth < maxDepth && depth >= 0;
    }
}
