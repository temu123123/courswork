package org.service.services.interfaces;

import org.service.dto.requests.HotelRequest;
import org.service.dto.responses.HotelResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface HotelService {
    HotelResponse getHotelById(Long id);
    List<HotelResponse> getAllHotels();
    HotelResponse addHotel(HotelRequest hotelRequest);
    HotelResponse updateHotel(Long id, HotelRequest hotelRequest);
    void deleteHotel(Long id);
}
