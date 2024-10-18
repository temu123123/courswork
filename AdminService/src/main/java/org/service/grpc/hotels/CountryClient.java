package org.service.grpc.hotels;

import hotels.Country.ProtoAddCountryRequest;
import hotels.Country.ProtoCountry;
import hotels.Country.ProtoDeleteCountryRequest;
import hotels.Country.ProtoGetAllCountriesResponse;
import hotels.Country.ProtoGetCountryByIdRequest;
import hotels.Country.ProtoUpdateCountryRequest;
import hotels.ProtoCountryServiceGrpc.ProtoCountryServiceBlockingStub;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.service.exceptions.CountryNotFoundException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CountryClient {

    @GrpcClient("countryService")
    private ProtoCountryServiceBlockingStub blockingStub;

    public ProtoCountry getCountryById(ProtoGetCountryByIdRequest request) {
        try {
            return blockingStub.getCountryById(request);
        } catch (StatusRuntimeException e) {
            log.error("Error while getting country by ID: {}", e.getStatus().getDescription());
            throw new CountryNotFoundException("Error while getting country by ID");
        }
    }

    public ProtoGetAllCountriesResponse getAllCountries() {
        try {
            return blockingStub.getAllCountries(com.google.protobuf.Empty.getDefaultInstance());
        } catch (StatusRuntimeException e) {
            log.error("Error while getting all countries: {}", e.getStatus().getDescription());
            throw e;
        }
    }

    public ProtoCountry addCountry(ProtoAddCountryRequest request) {
        try {
            return blockingStub.addCountry(request);
        } catch (StatusRuntimeException e) {
            log.error("Error while adding country: {}", e.getStatus().getDescription());
            throw e;
        }
    }

    public ProtoCountry updateCountry(ProtoUpdateCountryRequest request) {
        try {
            return blockingStub.updateCountry(request);
        } catch (StatusRuntimeException e) {
            log.error("Error while updating country: {}", e.getStatus().getDescription());
            throw new CountryNotFoundException("Error while updating country");
        }
    }

    public void deleteCountry(ProtoDeleteCountryRequest request) {
        try {
            blockingStub.deleteCountry(request);
        } catch (StatusRuntimeException e) {
            log.error("Error while deleting country: {}", e.getStatus().getDescription());
            throw new CountryNotFoundException("Error while deleting country");
        }
    }
}
