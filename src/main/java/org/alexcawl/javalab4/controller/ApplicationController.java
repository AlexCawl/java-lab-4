package org.alexcawl.javalab4.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.alexcawl.javalab4.model.dto.parsers.ParsingRequest;
import org.alexcawl.javalab4.model.dto.parsers.ParsingResponse;
import org.alexcawl.javalab4.model.dto.scanners.ScanningRequest;
import org.alexcawl.javalab4.model.dto.scanners.ScanningResponse;
import org.alexcawl.javalab4.model.dto.taskers.TaskingResponse;
import org.alexcawl.javalab4.model.dto.taskers.TaskingRequest;
import org.alexcawl.javalab4.service.parsers.ParsingService;
import org.alexcawl.javalab4.service.scanners.ScanningService;
import org.alexcawl.javalab4.service.taskers.TaskingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(name = "application-controller")
public class ApplicationController {
    @Autowired
    private TaskingService taskingService;

    @Autowired
    private ScanningService scanningService;

    @Autowired
    private ParsingService parsingService;

    @ApiOperation(value = "Request to crawl the site starting from this link to a certain depth")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Site crawl started successfully"),
            @ApiResponse(code = 400, message = "Some parameters were specified incorrectly"),
            @ApiResponse(code = 404, message = "The page at the specified URL was not found")
    })
    @RequestMapping(path = "/scan",
            name = "site-scanning-method",
            method = RequestMethod.POST,
            consumes = "application/json",
            produces = "application/json")
    public ResponseEntity<TaskingResponse> scan(@RequestBody TaskingRequest options) {
        return taskingService.scan(options.getUrl(), options.getDepth());
    }

    @ApiOperation(value = "Request to get the site structure")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "All site nodes in the database were found"),
            @ApiResponse(code = 400, message = "Invalid strategy")
    })
    @RequestMapping(path = "/analyze",
            name = "site-analyzing-method",
            method = RequestMethod.PUT,
            consumes = "application/json",
            produces = "application/json")
    public ResponseEntity<ScanningResponse> analyze(@RequestBody ScanningRequest options) {
        return scanningService.analyze(options.getArea(), options.getKey());
    }

    @ApiOperation(value = "Request to get data from the site nodes")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "All data from the nodes was found"),
            @ApiResponse(code = 400, message = "Invalid strategy")
    })
    @RequestMapping(path = "/parse",
            name = "site-parsing-method",
            method = RequestMethod.PUT,
            consumes = "application/json",
            produces = "application/json")
    public ResponseEntity<ParsingResponse> parse(@RequestBody ParsingRequest options) {
        return parsingService.parse(options.getArea(), options.getType(), options.getKey(), options.getLimit());
    }
}
