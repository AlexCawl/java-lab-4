package org.alexcawl.javalab4.controller;

import org.alexcawl.javalab4.model.dto.ApplicationConfigurationDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(name = "configuration-controller")
public class ConfigurationController {
    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${server.port}")
    private Integer applicationPort;

    @Value("${configuration.data.expires-in-hours}")
    private Integer dataExpiration;

    @Value("${configuration.crawler.depth}")
    private Integer maxDepth;

    @Value("${spring.datasource.url}")
    private String datasourceUrl;

    @RequestMapping(path = "/ping",
            name = "ping-api-method",
            method = RequestMethod.GET)
    public String ping() {
        return "pong!";
    }

    @RequestMapping(path = "/configuration",
            name = "config-api-method",
            method = RequestMethod.GET,
            produces = "application/json")
    public ResponseEntity<ApplicationConfigurationDTO> configuration() {
        return new ResponseEntity<>(
                new ApplicationConfigurationDTO(applicationName, applicationPort, dataExpiration, maxDepth, datasourceUrl),
                HttpStatus.OK
        );
    }
}
