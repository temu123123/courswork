package org.service.grpc;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import lombok.extern.slf4j.Slf4j;
import org.service.services.interfaces.CityService;
import org.service.services.interfaces.CountryService;
import org.service.services.interfaces.HotelService;
import org.service.services.interfaces.RoomService;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Slf4j
public class HotelServer {

    public final int port;
    public final Server server;

    public HotelServer(int port,
                       CityService cityService,
                       HotelService hotelService,
                       CountryService countryService,
                       RoomService roomService) {
        this(ServerBuilder.forPort(port), port,
                cityService, hotelService, countryService, roomService);
    }

    public HotelServer(ServerBuilder<?> serverBuilder,
                       int port,
                       CityService cityService,
                       HotelService hotelService,
                       CountryService countryService,
                       RoomService roomService) {
        this.port = port;
        serverBuilder.addService(new GrpcHotelService(hotelService));
        serverBuilder.addService(new GrpcCityService(cityService));
        serverBuilder.addService(new GrpcCountryService(countryService));
        serverBuilder.addService(new GrpcRoomService(roomService));
        this.server = serverBuilder.build();
    }

    public void start() throws IOException {
        server.start();
        log.info("Server started on port {}", port);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.error("Shutting down gRPC server because JVM is shutting down");
            try {
                HotelServer.this.stop();
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