package com.weyland.yutani.core.config;

import com.weyland.yutani.core.aspect.AuditAspect;
import com.weyland.yutani.core.aspect.AuditSender;
import com.weyland.yutani.core.aspect.impl.ConsoleAuditSender;
import com.weyland.yutani.core.aspect.impl.KafkaAuditSender;
import com.weyland.yutani.core.command.service.CommandExecutorService;
import com.weyland.yutani.core.command.service.impl.CommandExecutorServiceImpl;
import com.weyland.yutani.core.exception.handler.GlobalExceptionHandler;
import com.weyland.yutani.core.metrics.AndroidMetricsRecorder;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(WeylandYutaniProperties.class)
@Import(WeylandYutaniAutoConfiguration.AuditConfiguration.class)
public class WeylandYutaniAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ThreadPoolExecutor commonCommandExecutor(WeylandYutaniProperties properties) {
        WeylandYutaniProperties.ExecutorProperties executorProps = properties.getExecutor();
        return new ThreadPoolExecutor(
                executorProps.getCorePoolSize(),
                executorProps.getMaxPoolSize(),
                60L,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(executorProps.getQueueCapacity())
        );
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(MeterRegistry.class)
    public AndroidMetricsRecorder androidMetricsRecorder(MeterRegistry meterRegistry, ThreadPoolExecutor commonCommandExecutor) {
        AndroidMetricsRecorder recorder = new AndroidMetricsRecorder(meterRegistry);
        recorder.monitorExecutor(commonCommandExecutor); // Link the executor for queue monitoring
        return recorder;
    }

    @Bean
    @ConditionalOnMissingBean
    public CommandExecutorService commandExecutorService(ThreadPoolExecutor commonCommandExecutor,
                                                         ObjectProvider<AndroidMetricsRecorder> metricsRecorderProvider) {
        return new CommandExecutorServiceImpl(commonCommandExecutor, metricsRecorderProvider.getIfAvailable());
    }

    @Bean
    @ConditionalOnMissingBean
    public GlobalExceptionHandler globalExceptionHandler() {
        return new GlobalExceptionHandler();
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass(name = "org.aspectj.lang.annotation.Aspect")
    static class AuditConfiguration {

        @Bean
        @ConditionalOnMissingBean
        @ConditionalOnProperty(name = "weyland.yutani.audit.type", havingValue = "console", matchIfMissing = true)
        public AuditSender consoleAuditSender() {
            return new ConsoleAuditSender();
        }

        @Bean
        @ConditionalOnMissingBean
        @ConditionalOnProperty(name = "weyland.yutani.audit.type", havingValue = "kafka")
        @ConditionalOnClass(KafkaTemplate.class)
        public AuditSender kafkaAuditSender(KafkaTemplate<String, String> kafkaTemplate, WeylandYutaniProperties properties) {
            return new KafkaAuditSender(kafkaTemplate, properties.getAudit().getKafka().getTopic());
        }

        @Bean
        @ConditionalOnMissingBean
        public AuditAspect auditAspect(AuditSender auditSender) {
            return new AuditAspect(auditSender);
        }
    }
}
