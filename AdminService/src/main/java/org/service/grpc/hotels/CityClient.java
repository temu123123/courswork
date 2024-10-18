package org.service.grpc.hotels;

import hotels.City.ProtoAddCityRequest;
import hotels.City.ProtoCity;
import hotels.City.ProtoDeleteCityRequest;
import hotels.City.ProtoGetAllCitiesResponse;
import hotels.City.ProtoGetCityByIdRequest;
import hotels.City.ProtoUpdateCityRequest;
import hotels.ProtoCityServiceGrpc.ProtoCityServiceBlockingStub;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.service.exceptions.CityNotFoundException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CityClient {

    @GrpcClient("cityService")
    private ProtoCityServiceBlockingStub blockingStub;

    public ProtoCity getCityById(ProtoGetCityByIdRequest request) {
        try {
            return blockingStub.getCityById(request);
        } catch (StatusRuntimeException e) {
            log.error("Error while getting city by ID: {}", e.getStatus().getDescription());
            throw new CityNotFoundException("Error while getting city by ID");
        }
    }

    public ProtoGetAllCitiesResponse getAllCities() {
        try {
            return blockingStub.getAllCities(com.google.protobuf.Empty.getDefaultInstance());
        } catch (StatusRuntimeException e) {
            log.error("Error while getting all cities: {}", e.getStatus().getDescription());
            throw e;
        }
    }

    public ProtoCity addCity(ProtoAddCityRequest request) {
        try {
            return blockingStub.addCity(request);
        } catch (StatusRuntimeException e) {
            log.error("Error while adding city: {}", e.getStatus().getDescription());
            throw e;
        }
    }

    public ProtoCity updateCity(ProtoUpdateCityRequest request) {
        try {
            return blockingStub.updateCity(request);
        } catch (StatusRuntimeException e) {
            log.error("Error while updating city: {}", e.getStatus().getDescription());
            throw new CityNotFoundException("Error while updating city");
        }
    }

    public void deleteCity(ProtoDeleteCityRequest request) {
        try {
            blockingStub.deleteCity(request);
        } catch (StatusRuntimeException e) {
            log.error("Error while deleting city: {}", e.getStatus().getDescription());
            throw new CityNotFoundException("Error while deleting city");
        }
    }
}
