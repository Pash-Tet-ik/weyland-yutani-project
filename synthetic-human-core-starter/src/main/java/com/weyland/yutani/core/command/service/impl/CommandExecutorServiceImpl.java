package com.weyland.yutani.core.command.service.impl;

import com.weyland.yutani.core.command.dto.CommandDto;
import com.weyland.yutani.core.command.dto.CommandPriority;
import com.weyland.yutani.core.command.service.CommandExecutorService;
import com.weyland.yutani.core.exception.QueueOverloadException;
import com.weyland.yutani.core.metrics.AndroidMetricsRecorder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;

public class CommandExecutorServiceImpl implements CommandExecutorService {

    private static final Logger log = LoggerFactory.getLogger(CommandExecutorServiceImpl.class);

    private final ExecutorService commonCommandExecutor;
    private final AndroidMetricsRecorder metricsRecorder;

    public CommandExecutorServiceImpl(ExecutorService commonCommandExecutor, AndroidMetricsRecorder metricsRecorder) {
        this.commonCommandExecutor = commonCommandExecutor;
        this.metricsRecorder = metricsRecorder;
    }

    @Override
    public void processCommand(CommandDto command) {
        if (command.getPriority() == CommandPriority.CRITICAL) {
            executeCriticalCommand(command);
        } else {
            executeCommonCommand(command);
        }
    }

    private void executeCriticalCommand(CommandDto command) {
        CompletableFuture.runAsync(() -> {
            log.info("Executing CRITICAL command from author '{}': {}", command.getAuthor(), command.getDescription());
            metricsRecorder.incrementCompletedTasksCounter(command.getAuthor());
        }).exceptionally(ex -> {
            log.error("CRITICAL command execution failed for author '{}'", command.getAuthor(), ex);
            return null;
        });
    }

    private void executeCommonCommand(CommandDto command) {
        try {
            commonCommandExecutor.submit(() -> {
                log.info("Executing COMMON command from queue for author '{}': {}", command.getAuthor(), command.getDescription());
                metricsRecorder.incrementCompletedTasksCounter(command.getAuthor());
            });
        } catch (RejectedExecutionException e) {
            throw new QueueOverloadException("Cannot accept new COMMON command. The execution queue is full.");
        }
    }
}
