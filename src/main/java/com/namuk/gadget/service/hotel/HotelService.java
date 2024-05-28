package com.namuk.gadget.service.hotel;

import com.namuk.gadget.domain.City;
import com.namuk.gadget.domain.Hotel;
import com.namuk.gadget.dto.hotel.HotelRequestDTO;
import com.namuk.gadget.dto.hotel.HotelResponseDTO;

import java.util.List;
import java.util.Optional;

public interface HotelService {

    List<Hotel> getAllHotel();

    List<Hotel> getHotelByName(String name);

    // List<HotelResponseDTO> getHotelByType(Long type);

    List<HotelResponseDTO> getHotelByCityName(String name);

    List<HotelResponseDTO> getHotelByCityNameOrderByRoomCostDesc(String name);

    List<HotelResponseDTO> getHotelByCityNameOrderByRoomCostAsc(String name);

    Optional<List<HotelResponseDTO>> getHotelByNationName(String name);

    void insertHotel(HotelRequestDTO hotelRequestDto);
}
