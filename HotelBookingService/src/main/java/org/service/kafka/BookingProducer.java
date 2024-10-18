package org.service.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.Future;

@Service
@RequiredArgsConstructor
public class BookingProducer {

    private final KafkaProducer<Long, Long> kafkaProducer;

    public void sendMessage(String topic, Long key, Long value) {
        ProducerRecord<Long, Long> record = new ProducerRecord<>(topic, key, value);
        Future<RecordMetadata> future = kafkaProducer.send(record);

        try {
            RecordMetadata metadata = future.get();
            System.out.println("Message sent to topic " + metadata.topic() +
                    " with offset " + metadata.offset());
        } catch (Exception e) {
            System.err.println("Error sending message: " + e.getMessage());
        }
    }
}