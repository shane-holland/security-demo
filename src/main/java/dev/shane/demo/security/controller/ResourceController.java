package dev.shane.demo.security.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/")
public class ResourceController {

    @GetMapping
    public String getHome(final Principal principal) {
        return "Hello world: " + principal.getName() ;
    }
}
