package com.ahzx.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
/*
*
 * @date 2026/4/5 22:52
 */
@RestController
//@RequestMapping(value = "/calculate")
public class MainController {

    @RequestMapping("hello")
    @ResponseBody
    public String hello(HttpServletRequest request) {
        String username = request.getParameter("username");
        log.info("-------" + username);
        return "hello";
    }

    private final Logger log = LoggerFactory.getLogger(MainController.class);
    @RequestMapping(value = "/mortgage-info", method = RequestMethod.POST, consumes = "application/xml", produces =
            "application/json")
    @ResponseBody
    public String mortgage(@RequestBody String message) {

        return "hello";
    }

}
