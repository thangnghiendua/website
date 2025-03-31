package edu.uth.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class StaticController {

    @GetMapping(value = "/test-css", produces = "text/html")
    @ResponseBody
    public String testCss() {
        return "<html><head>"
             + "<style>"
             + "body { background-color: #f0f0f0; font-family: Arial; }"
             + "h1 { color: #4A90E2; }"
             + "</style>"
             + "</head><body>"
             + "<h1>CSS Test Page</h1>"
             + "<p>If you see this page with styled content, CSS is working!</p>"
             + "</body></html>";
    }
}