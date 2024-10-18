package org.service.grpc;

import com.google.protobuf.Empty;
import hotel.Hotel.ProtoAddHotelRequest;
import hotel.Hotel.ProtoDeleteHotelRequest;
import hotel.Hotel.ProtoGetAllHotelsResponse;
import hotel.Hotel.ProtoGetHotelByIdRequest;
import hotel.Hotel.ProtoHotel;
import hotel.Hotel.ProtoUpdateHotelRequest;
import hotel.ProtoHotelServiceGrpc.ProtoHotelServiceImplBase;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.service.dto.requests.HotelRequest;
import org.service.dto.responses.HotelResponse;
import org.service.exceptions.HotelNotFoundException;
import org.service.services.interfaces.HotelService;

import java.util.List;

@Slf4j
@GrpcService
@RequiredArgsConstructor
@Transactional
public class GrpcHotelService extends ProtoHotelServiceImplBase {

    private final HotelService service;

    private ProtoHotel newResponse(HotelResponse hotel) {
        return ProtoHotel.newBuilder()
                .setId(hotel.id())
                .setCityId(hotel.cityId())
                .setName(hotel.name())
                .build();
    }

    @Override
    public void getHotelById(ProtoGetHotelByIdRequest request, StreamObserver<ProtoHotel> responseObserver) {
        try {
            HotelResponse hotel = service.getHotelById(request.getId());
            ProtoHotel response = newResponse(hotel);
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (HotelNotFoundException e) {
            log.error("Hotel not found: {}", e.getMessage(), e);
            responseObserver.onError(Status.NOT_FOUND.withDescription("Hotel not found").asRuntimeException());
        } catch (Exception e) {
            log.error("Error while getting hotel by ID: {}", e.getMessage(), e);
            responseObserver.onError(Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException());
        }
    }

    @Override
    public void getAllHotels(Empty request, StreamObserver<ProtoGetAllHotelsResponse> responseObserver) {
        try {
            List<HotelResponse> hotels = service.getAllHotels();
            List<ProtoHotel> protoHotels = hotels.stream()
                    .map(this::newResponse)
                    .toList();
            ProtoGetAllHotelsResponse response = ProtoGetAllHotelsResponse.newBuilder()
                    .addAllHotels(protoHotels)
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            log.error("Error while getting all hotels: {}", e.getMessage(), e);
            responseObserver.onError(Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException());
        }
    }

    @Override
    public void addHotel(ProtoAddHotelRequest request, StreamObserver<ProtoHotel> responseObserver) {
        try {
            HotelRequest hotelRequest = new HotelRequest();
            hotelRequest.setName(request.getRequest().getName());
            hotelRequest.setCityId(request.getRequest().getCityId());
            HotelResponse newHotel = service.addHotel(hotelRequest);
            ProtoHotel response = newResponse(newHotel);
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            log.error("Error while adding hotel: {}", e.getMessage(), e);
            responseObserver.onError(Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException());
        }    }

    @Override
    public void updateHotel(ProtoUpdateHotelRequest request, StreamObserver<ProtoHotel> responseObserver) {
        try {
            HotelRequest hotelRequest = new HotelRequest();
            hotelRequest.setName(request.getHotel().getName());
            hotelRequest.setCityId(request.getHotel().getCityId());
            HotelResponse updatedHotel = service.updateHotel(request.getId(), hotelRequest);
            ProtoHotel response = newResponse(updatedHotel);
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (HotelNotFoundException e) {
            log.error("Hotel not found for update: {}", e.getMessage(), e);
            responseObserver.onError(Status.NOT_FOUND.withDescription("Hotel not found for update").asRuntimeException());
        } catch (Exception e) {
            log.error("Error while updating hotel: {}", e.getMessage(), e);
            responseObserver.onError(Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException());
        }    }

    @Override
    public void deleteHotel(ProtoDeleteHotelRequest request, StreamObserver<Empty> responseObserver) {
        try {
            service.deleteHotel(request.getId());
            responseObserver.onNext(Empty.getDefaultInstance());
            responseObserver.onCompleted();
        } catch (HotelNotFoundException e) {
            log.error("Hotel not found for deletion: {}", e.getMessage(), e);
            responseObserver.onError(Status.NOT_FOUND.withDescription("Hotel not found for deletion").asRuntimeException());
        } catch (Exception e) {
            log.error("Error while deleting hotel: {}", e.getMessage(), e);
            responseObserver.onError(Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException());
        }
    }
}
