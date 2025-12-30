package com.example.demo.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HashController {

    @GetMapping("/api/dev/hash")
    public String hash(@RequestParam("text") String text) {
        return new BCryptPasswordEncoder().encode(text);
    }
}
