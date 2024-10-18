package org.service.controllers.hotels;

import hotel.Hotel;
import hotel.Hotel.ProtoGetHotelByIdRequest;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.service.dto.hotels.requests.HotelRequest;
import org.service.dto.hotels.responses.HotelResponse;
import org.service.grpc.hotels.HotelClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/admin/hotels")
@RequiredArgsConstructor
@Slf4j
public class HotelController {

    private final HotelClient hotelClient;

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The request has succeeded"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal server error")})
    @GetMapping("/{id}")
    public ResponseEntity<HotelResponse> getHotelById(@PathVariable Long id) {
        ProtoGetHotelByIdRequest request = ProtoGetHotelByIdRequest.newBuilder()
                .setId(id)
                .build();
        Hotel.ProtoHotel protoHotel = hotelClient.getHotelById(request);

        HotelResponse response = new HotelResponse(
                protoHotel.getId(),
                protoHotel.getName(),
                protoHotel.getCityId()
        );

        return ResponseEntity.ok(response);
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The request has succeeded"),
            @ApiResponse(code = 500, message = "Internal server error")})
    @GetMapping
    public ResponseEntity<List<HotelResponse>> getAllHotels() {
        Hotel.ProtoGetAllHotelsResponse protoResponse = hotelClient.getAllHotels();
        List<HotelResponse> response = protoResponse.getHotelsList().stream()
                .map(protoHotel -> new HotelResponse(
                        protoHotel.getId(),
                        protoHotel.getName(),
                        protoHotel.getCityId()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 409, message = "Conflict"),
            @ApiResponse(code = 500, message = "Internal server error")})
    @PostMapping
    public ResponseEntity<HotelResponse> addHotel(@RequestBody HotelRequest request) {
        Hotel.ProtoAddHotelRequest protoRequest = Hotel.ProtoAddHotelRequest.newBuilder()
                .setRequest(Hotel.ProtoHotelRequest.newBuilder()
                        .setCityId(request.getCityId())
                        .setName(request.getName())
                        .build())
                .build();

        Hotel.ProtoHotel protoHotel = hotelClient.addHotel(protoRequest);

        HotelResponse response = new HotelResponse(
                protoHotel.getId(),
                protoHotel.getName(),
                protoHotel.getCityId()
        );

        return ResponseEntity.ok(response);
    }


    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated the hotel"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal server error")})
    @PutMapping("/{id}")
    public ResponseEntity<HotelResponse> updateHotel(@PathVariable Long id, @RequestBody HotelRequest request) {
        Hotel.ProtoUpdateHotelRequest protoRequest = Hotel.ProtoUpdateHotelRequest.newBuilder()
                .setHotel(Hotel.ProtoHotel.newBuilder()
                        .setId(id)
                        .setCityId(request.getCityId())
                        .setName(request.getName())
                        .build())
                .setId(id)
                .build();

        Hotel.ProtoHotel protoHotel = hotelClient.updateHotel(protoRequest);

        HotelResponse response = new HotelResponse(
                protoHotel.getId(),
                protoHotel.getName(),
                protoHotel.getCityId()
        );

        return ResponseEntity.ok(response);
    }

    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Successfully deleted the hotel"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal server error")})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHotel(@PathVariable Long id) {
        Hotel.ProtoDeleteHotelRequest protoRequest = Hotel.ProtoDeleteHotelRequest.newBuilder()
                .setId(id)
                .build();
        hotelClient.deleteHotel(protoRequest);
        return ResponseEntity.noContent().build();
    }
}

