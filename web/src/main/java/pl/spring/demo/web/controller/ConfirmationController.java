package pl.spring.demo.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ConfirmationController {
    @RequestMapping(value = "/confirmation", method = RequestMethod.GET)
    public String home() {
        return "bookConfirmation";
    }
}
