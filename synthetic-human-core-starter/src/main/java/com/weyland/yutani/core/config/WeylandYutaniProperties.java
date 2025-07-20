package com.weyland.yutani.core.config;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Setter
@Getter
@Validated
@ConfigurationProperties(prefix = "weyland.yutani")
public class WeylandYutaniProperties {

    @NotNull
    private AuditProperties audit = new AuditProperties();

    @NotNull
    private ExecutorProperties executor = new ExecutorProperties();

    @Setter
    @Getter
    public static class AuditProperties {
        private AuditType type = AuditType.CONSOLE;
        private Kafka kafka = new Kafka();

        @Setter
        @Getter
        public static class Kafka {
            @NotBlank
            private String topic = "weyland-yutani-audit-log";

        }
    }

    @Setter
    @Getter
    public static class ExecutorProperties {
        @Min(1)
        private int corePoolSize = 1;
        @Min(1)
        private int maxPoolSize = 1;
        @Min(1)
        private int queueCapacity = 100;

    }

    public enum AuditType {
        CONSOLE, KAFKA
    }
}
