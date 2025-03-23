package com.TMATS_BACKEND.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@Controller
public class authController {
    @GetMapping("/UserNotFound")
    public String userNotFound(Model model) {
        model.addAttribute("errorMessage", "Wrong credentials (User Not found)");
        return "error";
    }
    @GetMapping("/Wrongpassword")
    public String wrongPassword(Model model) {
        model.addAttribute("errorMessage", "Wrong credentials (Wrong password)");
        return "error";
    } 
    @GetMapping("/UserNotVerified")
    public String userNotVerified(@RequestParam(required = false) String email) {
        return "unverified";
    }
}
