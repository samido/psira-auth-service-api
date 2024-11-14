package com.psira.kafka.messages;

import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.annotation.PreDestroy;
import java.time.Duration;
import java.util.Collections;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

import org.jboss.logging.Logger;


@ApplicationScoped
public class KafkaConsumerService {
    private static final Logger LOGGER = Logger.getLogger(KafkaConsumerService.class);

    @Inject
    KafkaConsumer<String, String> consumer;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @PostConstruct
    public void startConsumer() {
        consumer.subscribe(Collections.singletonList("user-logins-topic2"));

        executorService.submit(() -> {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
                for (ConsumerRecord<String, String> record : records) {

                    System.out.println("Consumed message: " + record.value() +
                            ", partition: " + record.partition() +
                            ", offset: " + record.offset());
                    LOGGER.info( record.value());

                    int x = 1;
                    int y = 0;
                    int z = x / y;

                    System.out.println("z = " + z);
                    // Commit offsets if auto-commit is disabled
                    consumer.commitSync();
                }
            }
        });
    }

    @PreDestroy
    public void closeConsumer() {
        consumer.close();
        executorService.shutdown();
    }
}

