package org.service.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.service.dao.RoomRepository;
import org.service.dto.requests.RoomRequest;
import org.service.dto.responses.RoomResponse;
import org.service.entities.Room;
import org.service.exceptions.RoomNotFoundException;
import org.service.mappers.RoomMapper;
import org.service.services.interfaces.RoomService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
@Transactional
public class DefaultRoomService implements RoomService {
    private final RoomRepository repository;
    private final RoomMapper mapper;

    @Override
    public RoomResponse getRoomById(Long hotelId, Long id) {
        var roomEntity = repository.findAll()
                .stream()
                .filter(room -> room.getHotelId().equals(hotelId) && room.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RoomNotFoundException("Room not found: id = " + id));

        System.out.println("Room entity before mapping: " + roomEntity);
        RoomResponse response = mapper.EntityToResponse(roomEntity);
        System.out.println("Room response after mapping: " + response);

        return response;
    }

    @Override
    public List<RoomResponse> getAllRooms(Long hotelId) {
        Iterable<Room> rooms = repository.findAll();
        return StreamSupport.stream(rooms.spliterator(), false)
                .filter(room -> room.getHotelId().equals(hotelId))
                .map(mapper::EntityToResponse)
                .toList();
    }

    @Override
    public RoomResponse addRoom(Long hotelId, RoomRequest roomRequest) {
        var roomEntity = mapper.RequestToEntity(roomRequest);
        roomEntity.setHotelId(hotelId);
        repository.save(roomEntity);

        return mapper.EntityToResponse(roomEntity);
    }

    @Override
    public RoomResponse updateRoom(Long hotelId, Long id, RoomRequest roomRequest) {
        var existingRoom = repository.findAll()
                .stream()
                .filter(room -> room.getHotelId().equals(hotelId) && room.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RoomNotFoundException("Room not found: id = " + id));

        mapper.updateRoomFromRequest(roomRequest, existingRoom);

        var updatedRoom = repository.save(existingRoom);
        return mapper.EntityToResponse(updatedRoom);
    }

    @Override
    public void deleteRoom(Long hotelId, Long id) {
        var roomEntity = repository.findAll()
                .stream()
                .filter(room -> room.getHotelId().equals(hotelId) && room.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RoomNotFoundException("Room not found: id = " + id));

        repository.deleteById(id);
    }
}
