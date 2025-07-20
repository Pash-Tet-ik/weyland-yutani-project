package com.weyland.yutani.core.command.service;

import com.weyland.yutani.core.command.dto.CommandDto;


public interface CommandExecutorService {

    void processCommand(CommandDto command);
}
