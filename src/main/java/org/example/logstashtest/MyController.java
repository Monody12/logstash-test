package org.example.logstashtest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
public class MyController {

    private Logger logger = LoggerFactory.getLogger(MyController.class);

    @GetMapping
    public String hello() {
        var now = LocalDateTime.now();
        logger.info("{} Hello", now);
        return now + "Hello World  99961 - TEST";
    }

}
