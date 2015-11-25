package info.novatec.spring.boot.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Rest controller.
 */
@RestController
public class HelloController {

    @Value ("${hello.greeting}")
    private String greeting;

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public String message() {
        return "hello " + greeting;
    }
}
