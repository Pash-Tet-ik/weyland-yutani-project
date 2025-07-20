package com.weyland.yutani.bishop.service;

import com.weyland.yutani.core.annotation.WeylandWatchingYou;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class DiagnosticService {

    private static final Logger log = LoggerFactory.getLogger(DiagnosticService.class);

    @WeylandWatchingYou
    public String runSystemCheck(String systemName, int checkLevel) {
        log.info("Running diagnostic on system: {} at level {}", systemName, checkLevel);

        try {
            Thread.sleep(150);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return "System '" + systemName + "' reported status: OK";
    }
}
