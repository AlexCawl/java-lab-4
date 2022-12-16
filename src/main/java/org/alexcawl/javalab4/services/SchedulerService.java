package org.alexcawl.javalab4.services;

import com.google.common.hash.Hashing;
import lombok.extern.slf4j.Slf4j;
import org.alexcawl.javalab4.dao.Task;
import org.alexcawl.javalab4.dao.TaskRepository;
import org.alexcawl.javalab4.dao.WebResource;
import org.alexcawl.javalab4.dao.WebResourceRepository;
import org.alexcawl.javalab4.models.exceptions.EmptyTaskTableException;
import org.alexcawl.javalab4.models.exceptions.FileNotCreatedException;
import org.alexcawl.javalab4.models.exceptions.TaskValidationException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SchedulerService {
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private WebResourceRepository webResourceRepository;

    @Value("${configuration.data.path}")
    private String resourcePath;

    @Value("${configuration.data.expires-in-hours}")
    private Long expirationDate;

    @Value("${configuration.data.token-size}")
    private Integer tokenSize;

    /**
     * Main @Scheduled method.
     * Takes a task from the taskRepository and processes it.
     * O(n^2)
     * */
    @Scheduled(fixedRate = 1)
    public void process() {
        String url = null;
        try {
            Task task = taskRepository.findAll()
                    .stream()
                    .findAny()
                    .orElseThrow(EmptyTaskTableException::new);
            taskRepository.delete(task);
            url = task.getURL();
            validateTaskDepth(task);
            search(task);
        } catch (EmptyTaskTableException | TaskValidationException | DataIntegrityViolationException ignored) {
        } catch (IOException exception) {
            log.warn(String.format("Page is not available! %s", url));
        } catch (URISyntaxException exception) {
            log.warn(String.format("Invalid URL! %s", url));
        } catch (FileNotCreatedException exception) {
            log.error(String.format("File not created! %s", url));
        } catch (Exception exception) {
            log.error(String.format("Something gone very bad! %s", url));
        }
    }

    /**
     * O(n) + O(n^2)
     * */
    public void search(Task task) throws IOException, URISyntaxException, FileNotCreatedException {
        Optional<WebResource> resourceOptional = webResourceRepository.findByURL(task.getURL());
        if (resourceOptional.isPresent()) {
            WebResource resource = resourceOptional.get();
            if (!isDateValidInHours(resource.getLastUpdated(), new Date(), expirationDate)) {
                loadFromURL(task.getURL(), task.getDepthLimit());
            }
        } else {
            loadFromURL(task.getURL(), task.getDepthLimit());
        }
    }

    /**
     * O(n^2)
     * */
    private void loadFromStorage(String path, Integer depth) throws IOException, URISyntaxException {
        Document document = Jsoup.parse(new File(path), StandardCharsets.UTF_8.name());
        saveNewNodes(findNewNodes(document), depth);
    }

    /**
     * O(1) + O(1) + O(n^2) = O(n^2)
     * */
    private void loadFromURL(String url, Integer depth) throws URISyntaxException, FileNotCreatedException, IOException {
        Document document = Jsoup.connect(url)
                .followRedirects(false)
                .userAgent("Mozilla/5.0 (X11; Linux x86_64; rv:105.0) Gecko/20100101 Firefox/105.0")
                .get();
        String uri = document.baseUri();
        String ID = Hashing.sha256().hashString(uri, StandardCharsets.UTF_8).toString();
        String domain = new URI(uri).getHost();
        String path = constructPathFromID(resourcePath, ID, tokenSize);
        Date date = new Date();

        uploadAsFile(document, path, ID);
        webResourceRepository.save(new WebResource(ID, uri, domain, path, date));
        saveNewNodes(findNewNodes(document), depth);
    }

    /**
     * O(n^2)
     * */
    private void saveNewNodes(Set<String> nodes, Integer depth) {
        for (String node: nodes) {
            if (!taskRepository.existsByURL(node)) {
                taskRepository.save(new Task(node, depth - 1));
            }
        }
    }

    /**
     * O(1)
     * */
    private void validateTaskDepth(Task task) throws TaskValidationException {
        if (task.getDepthLimit() < 0) {
            throw new TaskValidationException();
        }
    }

    /**
     * O(1)
     * */
    private void uploadAsFile(Document document, String path, String name) throws FileNotCreatedException {
        try {
            Path directory = Files.createDirectories(Paths.get(path));
            try (BufferedWriter out = Files.newBufferedWriter(directory.resolve(name))) {
                out.write(String.valueOf(document.body()));
            } catch (IOException ignored) {}
        } catch (IOException exception) {
            throw new FileNotCreatedException();
        }
    }

    /**
     * O(n^2)
     * */
    public static Set<String> findNewNodes(Document document) throws URISyntaxException, MalformedURLException {
        String domainName = getDomainName(document.baseUri());
        return document.select("a[href]")
                .stream()
                .map(element -> element.attr("abs:href"))
                .filter(element -> {
                    try {
                        return getDomainName(element).equals(domainName);
                    } catch (URISyntaxException | NullPointerException | MalformedURLException e) {
                        return false;
                    }
                })
                .collect(Collectors.toSet());
    }

    /**
     * O(1)
     * */
    public static String getDomainName(String url) throws URISyntaxException, NullPointerException, MalformedURLException {
        return new URI(url).getHost();
    }

    /**
     * O(n)
     * */
    public static String constructPathFromID(String basePath, String ID, int partLength) {
        String [] array = new String [ID.length() / partLength + (ID.length() % partLength == 0 ? 0 : 1)];

        StringBuilder token = new StringBuilder();
        int counterSymbols = 0;
        int counterTokens = 0;

        for (int i = 0; i < ID.length(); i++) {
            token.append(ID.charAt(i));
            counterSymbols++;

            if (counterSymbols == partLength) {
                array[counterTokens] = token.toString();
                counterSymbols = 0;
                counterTokens++;
                token = new StringBuilder();
            }
        }

        if (!token.isEmpty()) {
            array[counterTokens] = token.toString();
        }

        return basePath + "/" + String.join("/", array);
    }

    /**
     * O(1)
     * */
    public static Boolean isDateValidInHours(Date dateWas, Date dateNow, Long differenceInHours) {
        long difference = dateNow.getTime() - dateWas.getTime();
        return TimeUnit.MILLISECONDS.toHours(difference) <= differenceInHours;
    }
}
