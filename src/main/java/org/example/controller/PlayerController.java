package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.model.dto.PlayerDTO;
import org.example.service.PlayerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/player")
@RequiredArgsConstructor
public class PlayerController {

    private final PlayerService playerService;

    // Used to display a list of players who are not assigned to any team in the frontend (Bonus)
    @GetMapping("/available-players")
    public List<PlayerDTO> getAvailablePlayers() {
        return playerService.getAvailablePlayers();
    }
}
