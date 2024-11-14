package com.psira.kafka.messages;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class TimedKafkaConsumer {
    private KafkaConsumer<String, String> consumer;

    public TimedKafkaConsumer(String bootstrapServers, String groupId, String topic) {
        // Set up Kafka consumer configuration
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        // Create and configure the Kafka consumer
        this.consumer = new KafkaConsumer<>(properties);
        this.consumer.subscribe(Collections.singletonList(topic));
    }

    public void consumeRecentMessages() {
        // Poll and process records
        while (true) {
            // Poll for records
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));

            // Get the current system time in milliseconds
            long currentTime = System.currentTimeMillis();

            for (ConsumerRecord<String, String> record : records) {
                // Check if the message is older than 10 seconds
                long messageAge = currentTime - record.timestamp();
                if (messageAge <= 10000) {  // 10 seconds in milliseconds
                    System.out.printf("Consumed record with key: %s, value: %s, age: %d ms%n",
                            record.key(), record.value(), messageAge);
                } else {
                    System.out.printf("Skipped record with key: %s, value: %s, age: %d ms (too old)%n",
                            record.key(), record.value(), messageAge);
                }
            }
        }
    }

    public void close() {
        consumer.close();
    }


    public static void main(String[] args) {
        // Define Kafka server, group ID, and topic
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
    }
}

