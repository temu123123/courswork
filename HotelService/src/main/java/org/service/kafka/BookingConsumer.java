package org.service.kafka;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.service.dto.requests.RoomRequest;
import org.service.dto.responses.RoomResponse;
import org.service.entities.Room;
import org.service.mappers.RoomMapper;
import org.service.services.interfaces.RoomService;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
public class BookingConsumer {

    private final KafkaConsumer<Long, Long> kafkaConsumer;
    private final RoomService service;
    private final RoomMapper mapper;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @PostConstruct
    public void start() {
        executorService.submit(() -> {
            kafkaConsumer.subscribe(Arrays.asList("bookingTopic", "bookingTopicDelete"));
            while (true) {
                ConsumerRecords<Long, Long> records = kafkaConsumer.poll(Duration.ofMillis(100));
                for (ConsumerRecord<Long, Long> record : records) {
                    RoomResponse response = service.getRoomById(record.key(), record.value());
                    Room room = mapper.ResponseToEntity(response);
                    if (record.topic().equals("bookingTopic")) {
                        room.setAvailable(false);
                    } else if (record.topic().equals("bookingTopicDelete")) {
                        room.setAvailable(true);
                    }
                    RoomRequest request = mapper.EntityToRequest(room);
                    service.updateRoom(record.key(), record.value(), request);
                }
            }
        });
    }
}