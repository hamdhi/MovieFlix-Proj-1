package com.myjwtai.jwtai.controller;

import com.myjwtai.jwtai.entity.Screen;
import com.myjwtai.jwtai.service.ScreenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/v1/screens")
public class ScreenController {

    @Autowired
    private ScreenService screenService;

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Screen> addScreen(@Valid @RequestBody Screen screen) {
        Screen savedScreen = screenService.addScreen(screen);
        return new ResponseEntity<>(savedScreen, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Screen> getScreenById(@PathVariable Long id) {
        Screen screen = screenService.getScreenById(id);
        return ResponseEntity.ok(screen);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Screen>> getAllScreens() {
        List<Screen> screens = screenService.getAllScreens();
        return ResponseEntity.ok(screens);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Screen> updateScreen(@PathVariable Long id, @Valid @RequestBody Screen screenDetails) {
        Screen updatedScreen = screenService.updateScreen(id, screenDetails);
        return ResponseEntity.ok(updatedScreen);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteScreen(@PathVariable Long id) {
        screenService.deleteScreen(id);
        return ResponseEntity.ok().body(new HashMap<String, String>() {{
            put("message", "Screen deleted successfully");
        }});
    }
}
