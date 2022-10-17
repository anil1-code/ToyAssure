package com.increff.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UIController {
    @Value("${app.baseUrl")
    public String baseUrl;

    @RequestMapping(value = "")
    public ModelAndView index() {
        System.out.println("I was here");
        return mav("index.html");
    }
    private ModelAndView mav(String page) {
        ModelAndView mav = new ModelAndView(page);
        mav.addObject("baseUrl", baseUrl);
        return mav;
    }
}
