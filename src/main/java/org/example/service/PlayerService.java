package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.model.dto.PlayerDTO;
import org.example.model.entity.PlayerEntity;
import org.example.model.mapper.PlayerMapper;
import org.example.model.repository.PlayerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final PlayerMapper playerMapper;

    public List<PlayerDTO> getAvailablePlayers() {
        List<PlayerEntity> availablePlayers = playerRepository.findByTeamIsNull();
        return availablePlayers.stream()
                .map(playerMapper::toDTO)
                .toList();
    }
}
