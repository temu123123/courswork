package org.service.grpc;

import com.google.protobuf.Empty;
import hotels.Country.ProtoAddCountryRequest;
import hotels.Country.ProtoCountry;
import hotels.Country.ProtoDeleteCountryRequest;
import hotels.Country.ProtoGetAllCountriesResponse;
import hotels.Country.ProtoGetCountryByIdRequest;
import hotels.Country.ProtoUpdateCountryRequest;
import hotels.ProtoCountryServiceGrpc.ProtoCountryServiceImplBase;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.service.dto.requests.CountryRequest;
import org.service.dto.responses.CountryResponse;
import org.service.exceptions.CountryNotFoundException;
import org.service.services.interfaces.CountryService;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@GrpcService
@RequiredArgsConstructor
@Transactional
public class GrpcCountryService extends ProtoCountryServiceImplBase {

    private final CountryService service;

    private ProtoCountry newResponse(CountryResponse country) {
        return ProtoCountry.newBuilder()
                .setId(country.id())
                .setName(country.name())
                .setDescription(country.description())
                .build();
    }

    @Override
    public void getCountryById(ProtoGetCountryByIdRequest request, StreamObserver<ProtoCountry> responseObserver) {
        try {
            CountryResponse country = service.getCountryById(request.getId());
            ProtoCountry response = newResponse(country);
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (CountryNotFoundException e) {
            log.error("Country not found: {}", e.getMessage());
            responseObserver.onError(Status.NOT_FOUND.withDescription("Country not found").asRuntimeException());
        } catch (Exception e) {
            log.error("Error while getting country by ID: {}", e.getMessage());
            responseObserver.onError(Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException());
        }
    }

    @Override
    public void getAllCountries(Empty request, StreamObserver<ProtoGetAllCountriesResponse> responseObserver) {
        try {
            List<CountryResponse> countries = service.getAllCountries();
            List<ProtoCountry> protoCountries = countries.stream().map(this::newResponse).collect(Collectors.toList());
            ProtoGetAllCountriesResponse response = ProtoGetAllCountriesResponse.newBuilder().addAllCountries(protoCountries).build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            log.error("Error while getting all countries: {}", e.getMessage());
            responseObserver.onError(Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException());
        }
    }

    @Override
    public void addCountry(ProtoAddCountryRequest request, StreamObserver<ProtoCountry> responseObserver) {
        try {
            CountryRequest countryRequest = new CountryRequest();
            countryRequest.setName(request.getRequest().getName());
            countryRequest.setDescription(request.getRequest().getDescription());
            CountryResponse newCountry = service.addCountry(countryRequest);
            ProtoCountry response = newResponse(newCountry);
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            log.error("Error while adding country: {}", e.getMessage());
            responseObserver.onError(Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException());
        }
    }

    @Override
    public void updateCountry(ProtoUpdateCountryRequest request, StreamObserver<ProtoCountry> responseObserver) {
        try {
            CountryRequest countryRequest = new CountryRequest();
            countryRequest.setName(request.getCountry().getName());
            countryRequest.setDescription(request.getCountry().getDescription());
            CountryResponse updatedCountry = service.updateCountry(request.getId(), countryRequest);
            ProtoCountry response = newResponse(updatedCountry);
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (CountryNotFoundException e) {
            log.error("Country not found for update: {}", e.getMessage());
            responseObserver.onError(Status.NOT_FOUND.withDescription("Country not found for update").asRuntimeException());
        } catch (Exception e) {
            log.error("Error while updating country: {}", e.getMessage());
            responseObserver.onError(Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException());
        }
    }

    @Override
    public void deleteCountry(ProtoDeleteCountryRequest request, StreamObserver<Empty> responseObserver) {
        try {
            service.deleteCountry(request.getId());
            responseObserver.onNext(Empty.getDefaultInstance());
            responseObserver.onCompleted();
        } catch (CountryNotFoundException e) {
            log.error("Country not found for deletion: {}", e.getMessage());
            responseObserver.onError(Status.NOT_FOUND.withDescription("Country not found for deletion").asRuntimeException());
        } catch (Exception e) {
            log.error("Error while deleting country: {}", e.getMessage());
            responseObserver.onError(Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException());
        }
    }
}
