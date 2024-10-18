package org.service.config;

import org.service.grpc.HotelServer;
import org.service.services.interfaces.CityService;
import org.service.services.interfaces.CountryService;
import org.service.services.interfaces.HotelService;
import org.service.services.interfaces.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class GrpcServerConfig {

    @Autowired
    private CityService cityService;

    @Autowired
    private HotelService hotelService;

    @Autowired
    private CountryService countryService;

    @Autowired
    private RoomService roomService;

    @Value("${grpc.server.port}")
    private int serverPort;

    @Bean
    public HotelServer serverStart() throws IOException {
        HotelServer server = new HotelServer(serverPort,
                cityService,
                hotelService,
                countryService,
                roomService);
        server.start();
        return server;
    }

}
