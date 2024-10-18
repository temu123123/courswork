package org.service.services.interfaces;

import org.service.dto.requests.RoomRequest;
import org.service.dto.responses.RoomResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RoomService {
    RoomResponse getRoomById(Long hotelId, Long id);
    List<RoomResponse> getAllRooms(Long hotelId);
    RoomResponse addRoom(Long hotelId, RoomRequest roomRequest);
    RoomResponse updateRoom(Long hotelId, Long id, RoomRequest roomRequest);
    void deleteRoom(Long hotelId, Long id);
}
