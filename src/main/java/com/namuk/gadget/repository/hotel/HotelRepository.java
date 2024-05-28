package com.namuk.gadget.repository.hotel;

import com.namuk.gadget.domain.Hotel;
import com.namuk.gadget.dto.hotel.HotelResponseDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface HotelRepository extends JpaRepository<Hotel, Long> {

    List<Hotel> findHotelByHotelName(String name);

    @Query
    List<HotelResponseDTO> findHotelByHotelType(Long type);

    /**
     * 도시 이름에 따라 특정 필드들을 포함한 호텔 정보를 객체를 생성한 DTO로 가져옴
     * @param name 검색할 도시의 이름
     * @return 지정된 도시에 속한 호텔들의 특정 필드들을 포함한 DTO 리스트
     */
    @Query(value = "select new com.namuk.gadget.dto.hotel.HotelResponseDTO(hp.hotelPictureUrl, h.hotelLocation, h.hotelName, h.hotelRating, h.hotelReviewCount, r.roomCost, r.roomPeople, c.cityName) " +
            "from Hotel h " +
            "join h.city c " +
            "join h.hotelPictureList hp " +
            "join h.hotelRoomList r " +
            "where c.cityName = :name")
    List<HotelResponseDTO> findHotelByCityName(String name);

    @Query(value = "select new com.namuk.gadget.dto.hotel.HotelResponseDTO(hp.hotelPictureUrl, h.hotelLocation, h.hotelName, h.hotelRating, h.hotelReviewCount, r.roomCost, r.roomPeople, c.cityName) " +
            "from Hotel h " +
            "join h.city c " +
            "join h.hotelPictureList hp " +
            "join h.hotelRoomList r " +
            "where c.cityName = :name " +
            "order by r.roomCost desc")
    List<HotelResponseDTO> findHotelByCityNameOrderByRoomCostDesc(String name);

    @Query(value = "select new com.namuk.gadget.dto.hotel.HotelResponseDTO(hp.hotelPictureUrl, h.hotelLocation, h.hotelName, h.hotelRating, h.hotelReviewCount, r.roomCost, r.roomPeople, c.cityName) " +
            "from Hotel h " +
            "join h.city c " +
            "join h.hotelPictureList hp " +
            "join h.hotelRoomList r " +
            "where c.cityName = :name " +
            "order by r.roomCost asc")
    List<HotelResponseDTO> findHotelByCityNameOrderByRoomCostAsc(String name);


    @Query(value = "select new com.namuk.gadget.dto.hotel.HotelResponseDTO(hp.hotelPictureUrl, h.hotelLocation, h.hotelName, h.hotelRating, h.hotelReviewCount, r.roomCost, r.roomPeople, c.cityName) " +
            "from Hotel h " +
            "join h.city c " +
            "join c.nation n " +
            "join h.hotelPictureList hp " +
            "join h.hotelRoomList r " +
            "where n.nationName = :name")
    List<HotelResponseDTO> findHotelByNationName(String name);
}
