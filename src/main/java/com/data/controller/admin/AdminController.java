package com.data.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.net.HttpCookie;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session) {
        // Check if user is logged in
        if (session.getAttribute("user") == null) {
            return "redirect:/admin/login";
        }
        return "admin/dashboard";
    }
}
