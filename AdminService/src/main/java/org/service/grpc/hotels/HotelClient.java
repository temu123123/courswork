package org.service.grpc.hotels;

import hotel.Hotel.ProtoAddHotelRequest;
import hotel.Hotel.ProtoDeleteHotelRequest;
import hotel.Hotel.ProtoGetAllHotelsResponse;
import hotel.Hotel.ProtoGetHotelByIdRequest;
import hotel.Hotel.ProtoHotel;
import hotel.Hotel.ProtoUpdateHotelRequest;
import hotel.ProtoHotelServiceGrpc.ProtoHotelServiceBlockingStub;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.service.exceptions.HotelNotFoundException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class HotelClient {

    @GrpcClient("hotelService")
    private ProtoHotelServiceBlockingStub blockingStub;

    public ProtoHotel getHotelById(ProtoGetHotelByIdRequest request) {
        try {
            return blockingStub.getHotelById(request);
        } catch (StatusRuntimeException e) {
            log.error("Error while getting hotel by ID: {}", e.getStatus().getDescription());
            throw new HotelNotFoundException("Error while getting hotel by ID");
        }
    }

    public ProtoGetAllHotelsResponse getAllHotels() {
        try {
            return blockingStub.getAllHotels(com.google.protobuf.Empty.getDefaultInstance());
        } catch (StatusRuntimeException e) {
            log.error("Error while getting all hotels: {}", e.getStatus().getDescription());
            throw e;
        }
    }

    public ProtoHotel addHotel(ProtoAddHotelRequest request) {
        try {
            return blockingStub.addHotel(request);
        } catch (StatusRuntimeException e) {
            log.error("Error while adding hotel: {}", e.getStatus().getDescription());
            throw e;
        }
    }

    public ProtoHotel updateHotel(ProtoUpdateHotelRequest request) {
        try {
            return blockingStub.updateHotel(request);
        } catch (StatusRuntimeException e) {
            log.error("Error while updating hotel: {}", e.getStatus().getDescription());
            throw new HotelNotFoundException("Error while updating hotel");
        }
    }

    public void deleteHotel(ProtoDeleteHotelRequest request) {
        try {
            blockingStub.deleteHotel(request);
        } catch (StatusRuntimeException e) {
            log.error("Error while deleting hotel: {}", e.getStatus().getDescription());
            throw new HotelNotFoundException("Error while deleting hotel");
        }
    }

}
