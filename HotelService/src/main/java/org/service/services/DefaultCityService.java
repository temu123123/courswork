package org.service.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.service.dao.CityRepository;
import org.service.dto.requests.CityRequest;
import org.service.dto.responses.CityResponse;
import org.service.entities.City;
import org.service.exceptions.CityNotFoundException;
import org.service.mappers.CityMapper;
import org.service.services.interfaces.CityService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
@Transactional
public class DefaultCityService implements CityService {
    private final CityRepository repository;
    private final CityMapper mapper;

    @Override
    public CityResponse getCityById(Long id) {
        var cityEntity = repository.findById(id)
                .orElseThrow(() -> new CityNotFoundException("City not found: id = " + id));

        return mapper.EntityToResponse(cityEntity);
    }

    @Override
    public List<CityResponse> getAllCities() {
        Iterable<City> cities = repository.findAll();
        return StreamSupport.stream(cities.spliterator(), false)
                .map(mapper::EntityToResponse)
                .toList();
    }

    @Override
    public CityResponse addCity(CityRequest cityRequest) {
        var cityEntity = mapper.RequestToEntity(cityRequest);
        repository.save(cityEntity);

        return mapper.EntityToResponse(cityEntity);
    }

    @Override
    public CityResponse updateCity(Long id, CityRequest cityRequest) {
        var existingCity = repository.findById(id)
                .orElseThrow(() -> new CityNotFoundException("City not found: id = " + id));

        mapper.updateCityFromRequest(cityRequest, existingCity);

        var updatedCity = repository.save(existingCity);
        return mapper.EntityToResponse(updatedCity);
    }

    @Override
    public void deleteCity(Long id) {
        if (!repository.existsById(id)) {
            throw new CityNotFoundException("City not found with ID: " + id);
        }

        repository.deleteById(id);
    }
}
