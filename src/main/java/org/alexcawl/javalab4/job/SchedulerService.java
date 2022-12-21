package org.alexcawl.javalab4.job;

import com.google.common.hash.Hashing;
import lombok.extern.slf4j.Slf4j;
import org.alexcawl.javalab4.dao.*;
import org.alexcawl.javalab4.model.exception.EmptyTaskTableException;
import org.alexcawl.javalab4.model.exception.TaskValidationException;
import org.alexcawl.javalab4.model.stuff.Status;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.UnsupportedMimeTypeException;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SchedulerService {
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private LogRepository logRepository;

    @Autowired
    private ResourceRepository resourceRepository;

    @Autowired
    private NodeRepository nodeRepository;

    @Autowired
    private ContentRepository contentRepository;

    @Value("${configuration.data.expires-in-hours}")
    private Long expirationDate;

    /**
     * Main @Scheduled method.
     * Takes a task from the taskRepository and processes it.
     * O(n^2)
     * */
    @Scheduled(fixedRate = 1)
    public void process() {
        try {
            Date date = new Date();
            Task task = taskRepository.findAll()
                    .stream()
                    .findAny()
                    .orElseThrow(EmptyTaskTableException::new);

            taskRepository.delete(task);

            try {
                validateTask(task);
                search(task);
            } catch (TaskValidationException | DataIntegrityViolationException ignored) {
                // vsem pohui + poebat'
            } catch (Exception exception) {
                exception.printStackTrace();
                log.error(String.format("Something gone very bad! %s", task.getURL()));
            }
            System.out.println("PROCESSING " + task.getURL() + " " + (new Date().getTime() - date.getTime()));
        } catch (EmptyTaskTableException ignored) {
        }
    }

    /**
     * O(n) + O(n^2)
     * */
    //FIXME
    public void search(Task task) {
        // мы обрабатываем Task, учитывая, что он такой один
        // Если в логах мы либо не обрабатывали ЭТО, либо его глубина была меньше чем сейчас, либо у ресурса истек срок годности
        Optional<Log> log = logRepository.findById(task.getID());
        if (log.isPresent()) {
            Optional<Resource> resource = resourceRepository.findById(task.getID());
            if (resource.isPresent() && !isDateValidInHours(resource.get().getLastUpdated(), new Date(), expirationDate)) {
                loadResourceFromURL(task.getURL(), task.getDepthLimit());
            } else if (log.get().getDepthLimit() < task.getDepthLimit()) {
                loadResourceFromURL(task.getURL(), task.getDepthLimit());
            } else if (resource.isEmpty()) {
                loadResourceFromURL(task.getURL(), task.getDepthLimit());
            }
        } else {
            loadResourceFromURL(task.getURL(), task.getDepthLimit());
        }
    }

    /**
     * O(1) + O(1) + O(n^2) = O(n^2)
     * */
    //FIXME
    private void loadResourceFromURL(String url, Integer depth) {
        try {
            validateURL(url);

            String ID = getHash(url);
            String domain = new URI(url).getHost();

            try {
                Date date = new Date();
                Document document = Jsoup.connect(url)
                        .followRedirects(false)
                        .userAgent("Mozilla/5.0 (X11; Linux x86_64; rv:105.0) Gecko/20100101 Firefox/105.0")
                        .get();
                System.out.println("LOADING " + url + " " + (new Date().getTime() - date.getTime()));

                /* Переопределяем на случай косяков в ссылке */
                url = document.baseUri();
                ID = getHash(url);
                domain = new URI(url).getHost();

                String body = String.valueOf(document.body());
                List<String> nodes = findNewNodes(document);

                if (body.isBlank() || body.equals("<body></body>")) {
                    nodeRepository.save(
                            new Node(ID, url, domain, depth, Status.EMPTY_CONTENT)
                    );
                } else {
                    nodeRepository.save(
                            new Node(ID, url, domain, depth, Status.OK)
                    );
                    resourceRepository.save(
                            new Resource(ID, url, domain, nodes.size(), String.join(" ", nodes))
                    );
                    contentRepository.save(
                            new Content(ID, body)
                    );
                }

                logRepository.save(
                        new Log(ID, url, depth)
                );
                writeNewTasks(nodes, depth);
            } catch (HttpStatusException exception) {
                nodeRepository.save(
                        new Node(ID, url, domain, depth, Status.NOT_FOUND)
                );
            } catch (MalformedURLException | DataIntegrityViolationException | UnsupportedMimeTypeException | IllegalArgumentException exception) {
                nodeRepository.save(
                        new Node(ID, url, domain, depth, Status.UNABLE_TO_LOAD)
                );
            } catch (IOException exception) {
                exception.printStackTrace();
                nodeRepository.save(
                        new Node(ID, url, domain, depth, Status.ERROR)
                );
            }
        } catch (URISyntaxException exception) {
            log.info("Invalid URL: " + url);
        }
    }

    /**
     * O(2n)
     * Смотрим для конкретного URL
     * Если в TaskRepository этого URL не существует &&
     * (в метрике не существует || в метрике существует, но с меньшей или равной высотой), то добавляем Task
     * */
    //FIXME
    private void writeNewTasks(List<String> nodes, Integer depth) {
        depth -= 1;
        for (String node: nodes) {
            // Обращаемся в Tasks, смотрим, существует ли? bool 1
            // Обращаемся в Log по сессии, существует ли?  bool 2

            // Если в Tasks есть, но с меньшим depth, обновляем depth и ВСЕ, иначе - НИЧЕГО
            // Если в Log есть, но с меньшим depth, обновляем depth и грузим как задачу
            String nodeHash = getHash(node);
            Optional<Task> task = taskRepository.findById(nodeHash);

            if (task.isPresent()) {
                Optional<Log> log = logRepository.findById(nodeHash);
                if (log.isPresent()) {
                    Log unboxedLog = log.get();
                    if (unboxedLog.getDepthLimit() < depth) {
                        taskRepository.save(new Task(node, depth));
                    }
                }
            } else {
                taskRepository.save(new Task(node, depth));
            }
        }
    }

    /**
     * O(1)
     * */
    private void validateTask(Task task) throws TaskValidationException {
        if (task.getDepthLimit() <= 0) {
            throw new TaskValidationException();
        }
    }

    /**
     * O(n^2)
     * */
    public static List<String> findNewNodes(Document document) throws URISyntaxException, MalformedURLException {
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
                .collect(Collectors.toSet())
                .stream().toList();
    }

    /**
     * O(1)
     * */
    public static String getDomainName(String url) throws URISyntaxException, NullPointerException, MalformedURLException {
        return new URI(url).getHost();
    }

    public static void validateURL(String url) throws URISyntaxException {
        new URI(url);
    }

    /**
     * O(1)
     * */
    public static Boolean isDateValidInHours(Date dateWas, Date dateNow, Long differenceInHours) {
        long difference = dateNow.getTime() - dateWas.getTime();
        return TimeUnit.MILLISECONDS.toHours(difference) <= differenceInHours;
    }

    /**
     * O(1)
     * */
    public static String getHash(String string) {
        return Hashing.sha256().hashString(string, StandardCharsets.UTF_8).toString();
    }
}
