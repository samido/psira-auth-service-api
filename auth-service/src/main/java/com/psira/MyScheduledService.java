package com.psira;

import com.psira.kafka.messages.TimedKafkaConsumer;
import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MyScheduledService {

    @Scheduled(every = "10s") // Executes every 10 seconds
    public void runEveryTenSeconds() {

        String bootstrapServers = "localhost:9092";
        String groupId = "timed-consumer-group";
        String topic = "user-logins-topic2";
        // Initialize the consumer
        TimedKafkaConsumer timedConsumer = new TimedKafkaConsumer(bootstrapServers, groupId, topic);

        // Consume recent messages
        try {
            timedConsumer.consumeRecentMessages();
        } finally {
            // Ensure consumer is closed gracefully
            timedConsumer.close();
        }
        System.out.println("This task runs every 10 seconds: " + System.currentTimeMillis());
    }
}
