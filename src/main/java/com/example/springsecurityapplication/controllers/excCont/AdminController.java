package com.example.springsecurityapplication.controllers.excCont;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
//@PreAuthorize("hasRole('ROLE_ADMIN') and hasRole('ROLE_USER')")
public class AdminController {
//    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @GetMapping("/admin")
    public String admin() {
        return "admin/admin";
    }
}

