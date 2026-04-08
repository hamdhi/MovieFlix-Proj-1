package com.myjwtai.jwtai.service;

import com.myjwtai.jwtai.entity.Screen;
import com.myjwtai.jwtai.repository.ScreenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ScreenService {

    @Autowired
    private ScreenRepository screenRepository;

    public Screen addScreen(Screen screen) {
        screen.setId(null);
        return screenRepository.save(screen);
    }

    public Screen getScreenById(Long id) {
        return screenRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error: Screen not found."));
    }

    public List<Screen> getAllScreens() {
        return screenRepository.findAll();
    }

    @Transactional
    public Screen updateScreen(Long id, Screen screenDetails) {
        Screen existingScreen = screenRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error: Screen not found."));
        
        existingScreen.setName(screenDetails.getName());
        existingScreen.setTotalSeats(screenDetails.getTotalSeats());
        
        return screenRepository.save(existingScreen);
    }

    @Transactional
    public void deleteScreen(Long id) {
        if (!screenRepository.existsById(id)) {
            throw new RuntimeException("Error: Screen not found.");
        }
        // Note: Deleting a screen could have cascading effects on Seats and Shows.
        // A soft delete or more complex logic would be needed in a real app.
        screenRepository.deleteById(id);
    }
}
