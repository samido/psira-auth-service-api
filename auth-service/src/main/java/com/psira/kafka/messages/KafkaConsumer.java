package com.psira.kafka.messages;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class KafkaConsumer {

    @Incoming("user-logins-topic2") // The name must match the channel in application.properties
    public void consume(String message) {
        System.out.println("Consumed message: " + message);
    }
}

