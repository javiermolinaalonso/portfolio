package com.assets.portfolio.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PortfolioController {

    @RequestMapping("/")
    String home() {
        return "Hello World!";
    }

}