package com.myjwtai.jwtai.controller;

import com.myjwtai.jwtai.entity.Screen;
import com.myjwtai.jwtai.repository.ScreenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/screens")
public class ScreenController {

    @Autowired
    private ScreenRepository screenRepository;

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Screen> addScreen(@RequestBody Screen screen) {
        screen.setId(null); // Ensure new screen
        Screen savedScreen = screenRepository.save(screen);
        return new ResponseEntity<>(savedScreen, HttpStatus.CREATED);
    }
}
