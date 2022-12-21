package org.alexcawl.javalab4.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/v2", name = "Life Status Controller")
public class LifeStatusController {
    @RequestMapping(path = "/ping",
            name = "ping-api-method",
            method = RequestMethod.GET,
            consumes = "application/json",
            produces = "application/json")
    public String ping() {
        return "pong!";
    }


}
