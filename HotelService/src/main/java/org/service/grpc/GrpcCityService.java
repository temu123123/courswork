package org.service.grpc;

import com.google.protobuf.Empty;
import hotels.City.ProtoAddCityRequest;
import hotels.City.ProtoCity;
import hotels.City.ProtoDeleteCityRequest;
import hotels.City.ProtoGetAllCitiesResponse;
import hotels.City.ProtoGetCityByIdRequest;
import hotels.City.ProtoUpdateCityRequest;
import hotels.ProtoCityServiceGrpc.ProtoCityServiceImplBase;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.service.dto.requests.CityRequest;
import org.service.dto.responses.CityResponse;
import org.service.exceptions.CityNotFoundException;
import org.service.services.interfaces.CityService;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@GrpcService
@RequiredArgsConstructor
@Transactional
public class GrpcCityService extends ProtoCityServiceImplBase {

    private final CityService service;

    private ProtoCity newResponse(CityResponse city) {
        return ProtoCity.newBuilder()
                .setId(city.id())
                .setName(city.name())
                .setDescription(city.description())
                .setCountryId(city.countryId())
                .build();
    }

    @Override
    public void getCityById(ProtoGetCityByIdRequest request, StreamObserver<ProtoCity> responseObserver) {
        try {
            CityResponse city = service.getCityById(request.getId());
            ProtoCity response = newResponse(city);
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (CityNotFoundException e) {
            log.error("City not found: {}", e.getMessage());
            responseObserver.onError(Status.NOT_FOUND.withDescription("City not found").asRuntimeException());
        } catch (Exception e) {
            log.error("Error while getting city by ID: {}", e.getMessage());
            responseObserver.onError(Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException());
        }
    }

    @Override
    public void getAllCities(Empty request, StreamObserver<ProtoGetAllCitiesResponse> responseObserver) {
        try {
            List<CityResponse> cities = service.getAllCities();
            List<ProtoCity> protoCities = cities.stream().map(this::newResponse).collect(Collectors.toList());
            ProtoGetAllCitiesResponse response = ProtoGetAllCitiesResponse.newBuilder().addAllCities(protoCities).build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            log.error("Error while getting all cities: {}", e.getMessage());
            responseObserver.onError(Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException());
        }
    }

    @Override
    public void addCity(ProtoAddCityRequest request, StreamObserver<ProtoCity> responseObserver) {
        try {
            CityRequest cityRequest = new CityRequest();
            cityRequest.setName(request.getRequest().getName());
            cityRequest.setCountryId(request.getRequest().getCountryId());
            cityRequest.setDescription(request.getRequest().getDescription());
            CityResponse newCity = service.addCity(cityRequest);
            ProtoCity response = newResponse(newCity);
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            log.error("Error while adding city: {}", e.getMessage());
            responseObserver.onError(Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException());
        }
    }

    @Override
    public void updateCity(ProtoUpdateCityRequest request, StreamObserver<ProtoCity> responseObserver) {
        try {
            CityRequest cityRequest = new CityRequest();
            cityRequest.setName(request.getCity().getName());
            cityRequest.setDescription(request.getCity().getDescription());
            cityRequest.setCountryId(request.getCity().getCountryId());
            CityResponse updatedCity = service.updateCity(request.getId(), cityRequest);
            ProtoCity response = newResponse(updatedCity);
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (CityNotFoundException e) {
            log.error("City not found for update: {}", e.getMessage());
            responseObserver.onError(Status.NOT_FOUND.withDescription("City not found for update").asRuntimeException());
        } catch (Exception e) {
            log.error("Error while updating city: {}", e.getMessage());
            responseObserver.onError(Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException());
        }
    }

    @Override
    public void deleteCity(ProtoDeleteCityRequest request, StreamObserver<Empty> responseObserver) {
        try {
            service.deleteCity(request.getId());
            responseObserver.onNext(Empty.getDefaultInstance());
            responseObserver.onCompleted();
        } catch (CityNotFoundException e) {
            log.error("City not found for deletion: {}", e.getMessage());
            responseObserver.onError(Status.NOT_FOUND.withDescription("City not found for deletion").asRuntimeException());
        } catch (Exception e) {
            log.error("Error while deleting city: {}", e.getMessage());
            responseObserver.onError(Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException());
        }
    }
}
