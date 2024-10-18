package org.service.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.service.dao.HotelRepository;
import org.service.dto.requests.HotelRequest;
import org.service.dto.responses.HotelResponse;
import org.service.entities.Hotel;
import org.service.exceptions.HotelNotFoundException;
import org.service.mappers.HotelMapper;
import org.service.services.interfaces.HotelService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
@Transactional
public class DefaultHotelService implements HotelService {
    private final HotelRepository repository;
    private final HotelMapper mapper;

    @Override
    public HotelResponse getHotelById(Long id) {
        var hotelEntity = repository.findById(id)
                .orElseThrow(() -> new HotelNotFoundException("Hotel not found: id = " + id));

        return mapper.EntityToResponse(hotelEntity);
    }

    @Override
    public List<HotelResponse> getAllHotels() {
        Iterable<Hotel> hotels = repository.findAll();
        return StreamSupport.stream(hotels.spliterator(), false)
                .map(mapper::EntityToResponse)
                .toList();
    }

    @Override
    public HotelResponse addHotel(HotelRequest hotelRequest) {
        var hotelEntity = mapper.RequestToEntity(hotelRequest);
        repository.save(hotelEntity);

        return mapper.EntityToResponse(hotelEntity);
    }

    @Override
    public HotelResponse updateHotel(Long id, HotelRequest hotelRequest) {
        var existingHotel = repository.findById(id)
                .orElseThrow(() -> new HotelNotFoundException("Hotel not found: id = " + id));

        mapper.updateHotelFromRequest(hotelRequest, existingHotel);

        var updatedHotel = repository.save(existingHotel);
        return mapper.EntityToResponse(updatedHotel);
    }

    @Override
    public void deleteHotel(Long id) {
        if(!repository.existsById(id)){
            throw new HotelNotFoundException("Hotel not found: id = " + id);
        }

        repository.deleteById(id);
    }
}
