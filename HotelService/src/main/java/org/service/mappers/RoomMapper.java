package org.service.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.service.dto.requests.RoomRequest;
import org.service.dto.responses.RoomResponse;
import org.service.entities.Room;

@Mapper(componentModel = "spring")
public interface RoomMapper {
    @Mapping(source = "available", target = "available")
    Room RequestToEntity(RoomRequest request);

    @Mapping(source = "available", target = "available")
    RoomRequest EntityToRequest(Room  room );

    @Mapping(source = "isAvailable", target = "available")
    Room  ResponseToEntity(RoomResponse response);

    @Mapping(source = "available", target = "isAvailable")
    RoomResponse EntityToResponse(Room  room);

    @Mapping(source = "available", target = "available")
    void updateRoomFromRequest(RoomRequest request, @MappingTarget Room  room);
}
