package info.novatec.spring.boot.controller;

import info.novatec.spring.boot.config.ApplicationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Rest controller.
 */
@RestController
public class HelloController {

    @Autowired
    private ApplicationProperties applicationProperties;

    @Value ("${hello.greeting}")
    private String greeting;

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public String message() {
        if (applicationProperties.isShowCurrentDate ()) {
            LocalDateTime localDateTime = LocalDateTime.now ();
            return String.format ( "hello %s. Today is:    %s",
                    greeting, localDateTime.format ( DateTimeFormatter.ISO_LOCAL_DATE_TIME ) );
        } else {
            return String.format ( "hello %s", greeting );
        }
    }
}
