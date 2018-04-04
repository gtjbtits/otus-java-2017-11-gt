package com.jbtits.otus.lecture15.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/")
public class PageController {
    @RequestMapping(method = RequestMethod.GET)
    public String printStatistics(ModelMap model) {
        return "chat";
    }
}
