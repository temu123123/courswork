package org.service.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.service.dao.CountryRepository;
import org.service.dto.requests.CountryRequest;
import org.service.dto.responses.CountryResponse;
import org.service.entities.Country;
import org.service.exceptions.CountryNotFoundException;
import org.service.mappers.CountryMapper;
import org.service.services.interfaces.CountryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
@Transactional
public class DefaultCountryService implements CountryService {
    private final CountryRepository repository;
    private final CountryMapper mapper;

    @Override
    public CountryResponse getCountryById(Long id) {
        var countryEntity = repository.findById(id)
                .orElseThrow(() -> new CountryNotFoundException("Country not found: id = " + id));

        return mapper.EntityToResponse(countryEntity);
    }

    @Override
    public List<CountryResponse> getAllCountries() {
        Iterable<Country> countries = repository.findAll();
        return StreamSupport.stream(countries.spliterator(), false)
                .map(mapper::EntityToResponse)
                .toList();
    }

    @Override
    public CountryResponse addCountry(CountryRequest countryRequest) {
        var countryEntity = mapper.RequestToEntity(countryRequest);
        repository.save(countryEntity);

        return mapper.EntityToResponse(countryEntity);
    }

    @Override
    public CountryResponse updateCountry(Long id, CountryRequest countryRequest) {
        var existingCountry = repository.findById(id)
                .orElseThrow(() -> new CountryNotFoundException("Country not found: id = " + id));

        mapper.updateCountryFromRequest(countryRequest, existingCountry);

        var updatedCountry = repository.save(existingCountry);
        return mapper.EntityToResponse(updatedCountry);
    }

    @Override
    public void deleteCountry(Long id) {
        if (!repository.existsById(id)) {
            throw new CountryNotFoundException("Country not found: id = " + id);
        }

        repository.deleteById(id);
    }
}
