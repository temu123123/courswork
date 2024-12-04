package org.service.controllers.hotels;

import hotels.Room.ProtoAddRoomRequest;
import hotels.Room.ProtoDeleteRoomRequest;
import hotels.Room.ProtoGetAllRoomsRequest;
import hotels.Room.ProtoGetAllRoomsResponse;
import hotels.Room.ProtoGetRoomByIdRequest;
import hotels.Room.ProtoRoom;
import hotels.Room.ProtoRoomRequest;
import hotels.Room.ProtoUpdateRoomRequest;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.service.dto.hotels.requests.RoomRequest;
import org.service.dto.hotels.responses.RoomResponse;
import org.service.grpc.hotels.RoomClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/admin/hotels/{hotelId}/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomClient roomClient;

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The request has succeeded"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal server error")})
    @GetMapping("/{id}")
    public ResponseEntity<RoomResponse> getRoomById(@PathVariable Long hotelId, @PathVariable Long id) {
        ProtoGetRoomByIdRequest request = ProtoGetRoomByIdRequest.newBuilder()
                .setHotelId(hotelId)
                .setId(id)
                .build();
        ProtoRoom protoRoom = roomClient.getRoomById(request);

        RoomResponse response = new RoomResponse(
                protoRoom.getId(),
                protoRoom.getHotelId(),
                protoRoom.getType(),
                protoRoom.getPrice(),
                protoRoom.getIsAvailable()
        );

        return ResponseEntity.ok(response);
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The request has succeeded"),
            @ApiResponse(code = 500, message = "Internal server error")})
    @GetMapping
    public ResponseEntity<List<RoomResponse>> getAllRooms(@PathVariable Long hotelId) {
        ProtoGetAllRoomsRequest request = ProtoGetAllRoomsRequest.newBuilder()
                .setHotelId(hotelId)
                .build();
        ProtoGetAllRoomsResponse protoResponse = roomClient.getAllRooms(request);
        List<RoomResponse> response = protoResponse.getRoomsList().stream()
                .map(protoRoom -> new RoomResponse(
                        protoRoom.getId(),
                        protoRoom.getHotelId(),
                        protoRoom.getType(),
                        protoRoom.getPrice(),
                        protoRoom.getIsAvailable()
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
    public ResponseEntity<RoomResponse> addRoom(@PathVariable Long hotelId, @RequestBody RoomRequest roomRequest) {
        ProtoAddRoomRequest protoRequest = ProtoAddRoomRequest.newBuilder()
                .setRequest(ProtoRoomRequest.newBuilder()
                        .setPrice(roomRequest.getPrice())
                        .setType(roomRequest.getType())
                        .setIsAvailable(roomRequest.isAvailable())
                        .build())
                .setHotelId(hotelId)
                .build();

        System.out.println(protoRequest.getRequest().getIsAvailable());

        ProtoRoom protoRoom = roomClient.addRoom(protoRequest);

        RoomResponse response = new RoomResponse(
                protoRoom.getId(),
                protoRoom.getHotelId(),
                protoRoom.getType(),
                protoRoom.getPrice(),
                protoRoom.getIsAvailable()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Successfully deleted the room"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal server error")})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long hotelId, @PathVariable Long id) {
        ProtoDeleteRoomRequest protoRequest = ProtoDeleteRoomRequest.newBuilder()
                .setId(id)
                .setHotelId(hotelId)
                .build();
        roomClient.deleteRoom(protoRequest);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated the room"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal server error")})
    @PutMapping("/{id}")
    public ResponseEntity<RoomResponse> updateRoom(@PathVariable Long hotelId, @PathVariable Long id, @RequestBody RoomRequest roomRequest) {
        ProtoUpdateRoomRequest protoRequest = ProtoUpdateRoomRequest.newBuilder()
                .setRoom(ProtoRoomRequest.newBuilder()
                        .setPrice(roomRequest.getPrice())
                        .setType(roomRequest.getType())
                        .setIsAvailable(roomRequest.isAvailable())
                        .build())
                .setHotelId(hotelId)
                .setId(id)
                .build();

        ProtoRoom protoRoom = roomClient.updateRoom(protoRequest);

        RoomResponse response = new RoomResponse(
                protoRoom.getId(),
                protoRoom.getHotelId(),
                protoRoom.getType(),
                protoRoom.getPrice(),
                protoRoom.getIsAvailable()
        );

        return ResponseEntity.ok(response);
    }
}

