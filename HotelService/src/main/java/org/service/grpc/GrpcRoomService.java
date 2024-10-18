package org.service.grpc;

import com.google.protobuf.Empty;
import hotels.ProtoRoomServiceGrpc.ProtoRoomServiceImplBase;
import hotels.Room.ProtoAddRoomRequest;
import hotels.Room.ProtoDeleteRoomRequest;
import hotels.Room.ProtoGetAllRoomsRequest;
import hotels.Room.ProtoGetAllRoomsResponse;
import hotels.Room.ProtoGetRoomByIdRequest;
import hotels.Room.ProtoRoom;
import hotels.Room.ProtoUpdateRoomRequest;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.service.dto.requests.RoomRequest;
import org.service.dto.responses.RoomResponse;
import org.service.exceptions.RoomNotFoundException;
import org.service.services.interfaces.RoomService;

import java.util.List;

@Slf4j
@GrpcService
@RequiredArgsConstructor
@Transactional
public class GrpcRoomService extends ProtoRoomServiceImplBase {

    private final RoomService service;

    private ProtoRoom newResponse(RoomResponse room) {
        return ProtoRoom.newBuilder()
                .setId(room.id())
                .setHotelId(room.hotelId())
                .setType(room.type())
                .setPrice(room.price())
                .setIsAvailable(room.isAvailable())
                .build();
    }

    @Override
    public void getRoomById(ProtoGetRoomByIdRequest request, StreamObserver<ProtoRoom> responseObserver) {
        try {
            RoomResponse room = service.getRoomById(request.getHotelId(), request.getId());
            ProtoRoom response = newResponse(room);
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (RoomNotFoundException e) {
            log.error("Room not found: {}", e.getMessage(), e);
            responseObserver.onError(Status.NOT_FOUND.withDescription("Room not found").asRuntimeException());
        } catch (Exception e) {
            log.error("Error while getting room by ID: {}", e.getMessage(), e);
            responseObserver.onError(Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException());
        }
    }

    @Override
    public void getAllRooms(ProtoGetAllRoomsRequest request, StreamObserver<ProtoGetAllRoomsResponse> responseObserver) {
        try {
            List<RoomResponse> rooms = service.getAllRooms(request.getHotelId());
            List<ProtoRoom> protoRooms = rooms.stream()
                    .map(this::newResponse)
                    .toList();
            ProtoGetAllRoomsResponse response = ProtoGetAllRoomsResponse.newBuilder().addAllRooms(protoRooms).build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            log.error("Error while getting all rooms: {}", e.getMessage(), e);
            responseObserver.onError(Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException());
        }
    }

    @Override
    public void addRoom(ProtoAddRoomRequest request, StreamObserver<ProtoRoom> responseObserver) {
        try {
            RoomRequest roomRequest = new RoomRequest();
            roomRequest.setType(request.getRequest().getType());
            roomRequest.setPrice(request.getRequest().getPrice());
            roomRequest.setAvailable(request.getRequest().getIsAvailable());
            RoomResponse newRoom = service.addRoom(request.getHotelId(), roomRequest);
            ProtoRoom response = newResponse(newRoom);
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            log.error("Error while adding room: {}", e.getMessage(), e);
            responseObserver.onError(Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException());
        }
    }

    @Override
    public void updateRoom(ProtoUpdateRoomRequest request, StreamObserver<ProtoRoom> responseObserver) {
        try {
            RoomRequest roomRequest = new RoomRequest();
            roomRequest.setType(request.getRoom().getType());
            roomRequest.setPrice(request.getRoom().getPrice());
            roomRequest.setAvailable(request.getRoom().getIsAvailable());
            RoomResponse updatedRoom = service.updateRoom(request.getHotelId(), request.getId(), roomRequest);
            ProtoRoom response = newResponse(updatedRoom);
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (RoomNotFoundException e) {
            log.error("Room not found for update: {}", e.getMessage(), e);
            responseObserver.onError(Status.NOT_FOUND.withDescription("Room not found for update").asRuntimeException());
        } catch (Exception e) {
            log.error("Error while updating room: {}", e.getMessage(), e);
            responseObserver.onError(Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException());
        }
    }

    @Override
    public void deleteRoom(ProtoDeleteRoomRequest request, StreamObserver<Empty> responseObserver) {
        try {
            service.deleteRoom(request.getHotelId() ,request.getId());
            responseObserver.onNext(Empty.getDefaultInstance());
            responseObserver.onCompleted();
        } catch (RoomNotFoundException e) {
            log.error("Room not found for deletion: {}", e.getMessage(), e);
            responseObserver.onError(Status.NOT_FOUND.withDescription("Room not found for deletion").asRuntimeException());
        } catch (Exception e) {
            log.error("Error while deleting room: {}", e.getMessage(), e);
            responseObserver.onError(Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException());
        }
    }
}
