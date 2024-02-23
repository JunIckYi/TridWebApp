package com.sele.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
public class HomeController {
	
    @RequestMapping(value = {"", "/"}, method = RequestMethod.GET)
    public String index2(Model model) {
    	
        return "index";
    }

    @GetMapping(value = "/index")
    public String index3(Model model) {
    	
        return "index";
    }

}
