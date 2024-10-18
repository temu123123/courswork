package org.service.grpc;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import lombok.extern.slf4j.Slf4j;
import org.service.services.BookingService;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Slf4j
public class BookingServer {

    public final int port;
    public final Server server;

    public BookingServer(int port,
                         BookingService bookingService) {
        this(ServerBuilder.forPort(port), port,
                bookingService);
    }

    public BookingServer(ServerBuilder<?> serverBuilder,
                       int port,
                       BookingService bookingService) {
        this.port = port;
        serverBuilder.addService(new GrpcBookingService(bookingService));
        this.server = serverBuilder.build();
    }

    public void start() throws IOException {
        server.start();
        log.info("Server started on port {}", port);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.error("Shutting down gRPC server because JVM is shutting down");
            try {
                BookingServer.this.stop();
            } catch (InterruptedException e) {
                log.error("Error while stopping server: {}", e.getMessage());
            }
            log.info("Server shut down");
        }));
    }

    public void stop() throws InterruptedException {
        if (server != null) {
            server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
        }
    }
}