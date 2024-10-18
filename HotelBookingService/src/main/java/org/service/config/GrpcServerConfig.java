package org.service.config;

import org.service.grpc.BookingServer;
import org.service.services.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class GrpcServerConfig {

    @Autowired
    private BookingService bookingService;

    @Value("${grpc.server.port}")
    private int serverPort;

    @Bean
    public BookingServer serverStart() throws IOException {
        BookingServer server = new BookingServer(serverPort,
                bookingService);
        server.start();
        return server;
    }

}

