package com.teamcrocodile.tripwire.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RootController {

    @GetMapping("/")
    public String redirectToLogin() {
        return "redirect:/login.html";
    }

    @GetMapping("/accounts")
    public String accountsPage() {
        return "redirect:/accounts.html";
    }
}
