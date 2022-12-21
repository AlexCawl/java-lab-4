package org.alexcawl.javalab4.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/v1", name = "Base Controller")
public class BaseController {
    @RequestMapping(path = "/ping", name = "ping", method = RequestMethod.GET)
    public String ping() {
        return "pong!";
    }
}
