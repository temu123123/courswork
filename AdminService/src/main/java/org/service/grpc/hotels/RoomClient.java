package org.service.grpc.hotels;

import hotels.ProtoRoomServiceGrpc.ProtoRoomServiceBlockingStub;
import hotels.Room.ProtoAddRoomRequest;
import hotels.Room.ProtoDeleteRoomRequest;
import hotels.Room.ProtoGetAllRoomsRequest;
import hotels.Room.ProtoGetAllRoomsResponse;
import hotels.Room.ProtoGetRoomByIdRequest;
import hotels.Room.ProtoRoom;
import hotels.Room.ProtoUpdateRoomRequest;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.service.exceptions.RoomNotFoundException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RoomClient {

    @GrpcClient("roomService")
    private ProtoRoomServiceBlockingStub blockingStub;

    public ProtoRoom getRoomById(ProtoGetRoomByIdRequest request) {
        try {
            return blockingStub.getRoomById(request);
        } catch (StatusRuntimeException e) {
            log.error("Error while getting room by ID: {}", e.getStatus().getDescription());
            throw new RoomNotFoundException("Error while getting room by ID");
        }
    }

    public ProtoGetAllRoomsResponse getAllRooms(ProtoGetAllRoomsRequest request) {
        try {
            return blockingStub.getAllRooms(request);
        } catch (StatusRuntimeException e) {
            log.error("Error while getting all rooms: {}", e.getStatus().getDescription());
            throw e;
        }
    }

    public ProtoRoom addRoom(ProtoAddRoomRequest request) {
        try {
            return blockingStub.addRoom(request);
        } catch (StatusRuntimeException e) {
            log.error("Error while adding room: {}", e.getStatus().getDescription());
            throw e;
        }
    }

    public ProtoRoom updateRoom(ProtoUpdateRoomRequest request) {
        try {
            return blockingStub.updateRoom(request);
        } catch (StatusRuntimeException e) {
            log.error("Error while updating room: {}", e.getStatus().getDescription());
            throw new RoomNotFoundException("Error while updating room");
        }
    }

    public void deleteRoom(ProtoDeleteRoomRequest request) {
        try {
            blockingStub.deleteRoom(request);
        } catch (StatusRuntimeException e) {
            log.error("Error while deleting room: {}", e.getStatus().getDescription());
            throw new RoomNotFoundException("Error while deleting room");
        }
    }
}
