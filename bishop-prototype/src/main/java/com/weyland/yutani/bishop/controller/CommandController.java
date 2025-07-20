package com.weyland.yutani.bishop.controller;

import com.weyland.yutani.core.annotation.WeylandWatchingYou;
import com.weyland.yutani.core.command.dto.CommandDto;
import com.weyland.yutani.core.command.service.CommandExecutorService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/commands")
public class CommandController {

    private final CommandExecutorService commandExecutorService;

    public CommandController(CommandExecutorService commandExecutorService) {
        this.commandExecutorService = commandExecutorService;
    }

    @PostMapping
    public ResponseEntity<Void> submitCommand(@Valid @RequestBody CommandDto command) {
        commandExecutorService.processCommand(command);
        return ResponseEntity.accepted().build();
    }
}
