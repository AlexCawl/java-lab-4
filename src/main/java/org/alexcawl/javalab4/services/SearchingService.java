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
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SearchingService {
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private WebResourceRepository webResourceRepository;

    @Value("${resources.path}")
    private String resourcePath;

    @Value("${resources.expiration}")
    private Long expirationDate;

    /**
     * Main @Scheduled method.
     * Takes a task from the taskRepository and processes it.
     * */
    @Scheduled(fixedRate = 1)
    public void process() {
        try {
            Task task = taskRepository.findAll()
                    .stream()
                    .findAny()
                    .orElseThrow(EmptyTaskTableException::new);
            taskRepository.delete(task);
            validateTask(task);
            search(task);
        } catch (EmptyTaskTableException | TaskValidationException ignored) {
        } catch (IOException exception) {
            //log.warn("Page is not available!");
        } catch (URISyntaxException exception) {
            log.warn("Invalid URL!");
        } catch (FileNotCreatedException exception) {
            log.warn("File not created!");
        } catch (DataIntegrityViolationException exception) {
            //log.warn("Already exists!");
        } catch (Exception exception) {
            //exception.printStackTrace();
            log.error("Something gone very bad!");
        }
    }

    /**
     *
     * */
    public void search(Task task) throws IOException, URISyntaxException, FileNotCreatedException {
        Optional<WebResource> resourceOptional = webResourceRepository.findByURL(task.getURL());
        if (resourceOptional.isPresent()) {
            WebResource resource = resourceOptional.get();
            /*
            * сделать анализ страницы из внутреннего хранилища и запись новых нодов оттуда
            * */
        } else {
            load(task.getURL(), task.getDepthLimit());
        }
    }

    /**
     *
     * */
    private void load(String url, Integer depth) throws URISyntaxException, FileNotCreatedException, IOException {
        Document document = Jsoup.connect(url).followRedirects(false).get();
        String uri = document.baseUri();
        String ID = Hashing.sha256().hashString(uri, StandardCharsets.UTF_8).toString();
        String domain = new URI(uri).getHost();
        String path = constructPathFromID(resourcePath, ID, 8);
        Date date = new Date();

        uploadAsFile(document, path, ID);
        Set<String> nodes = findNewNodes(document);
        webResourceRepository.save(new WebResource(ID, uri, domain, path, date));

        for (String node: nodes) {
            taskRepository.save(new Task(node, depth - 1));
        }
    }

    /**
     *
     * */
    public void validateTask(Task task) throws TaskValidationException {
        if (task.getDepthLimit() < 0) {
            throw new TaskValidationException();
        }
    }

    /**
     *
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
     *
     * */
    private Set<String> findNewNodes(Document document) throws URISyntaxException, MalformedURLException {
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

    private String getDomainName(String url) throws URISyntaxException, NullPointerException, MalformedURLException {
        return new URI(url).getHost();
    }

    /**
     *
     * */
    private static String constructPathFromID(String basePath, String ID, int partLength) {
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
}
