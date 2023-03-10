package org.alexcawl.javalab4.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/v0")
public class PingController {
    @GetMapping(value = "/ping")
    public String ping() {
        return "pong!";
    }
}
