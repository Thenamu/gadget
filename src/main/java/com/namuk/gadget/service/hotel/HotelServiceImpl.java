package com.namuk.gadget.service.hotel;

import com.namuk.gadget.controller.SearchController;
import com.namuk.gadget.domain.Hotel;
import com.namuk.gadget.dto.hotel.HotelRequestDTO;
import com.namuk.gadget.dto.hotel.HotelResponseDTO;
import com.namuk.gadget.repository.hotel.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HotelServiceImpl implements HotelService{

    private final HotelRepository hotelRepository;

    @Autowired
    public HotelServiceImpl(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    /**
     * 리포지토리에서 모든 호텔을 가져옴
     *
     * @return 리포지토리에 저장된 모든 호텔의 필드 리스트
     */
    @Override
    public List<Hotel> getAllHotel() {
        return hotelRepository.findAll();
    }

    /**
     * 이름에 따라 리포지토리에서 호텔을 가져옴
     *
     * @param name 검색할 호텔의 이름
     * @return 지정된 이름을 가진 호텔의 필드 리스트
     */
    @Override
    public List<Hotel> getHotelByName(String name) {
        return hotelRepository.findHotelByHotelName(name);
    }

//    @Override
//    public List<HotelResponseDTO> getHotelByType(Long type) {
//        return hotelRepository.
//    }

    /**
     * hotelRequestDto.hotel()을 기반으로 호텔 엔티티에 삽입
     *
     * @param hotelRequestDto 삽입할 호텔에 대한 정보를 포함하는 DTO
     */
    @Override
    public void insertHotel(HotelRequestDTO hotelRequestDto) {
        hotelRepository.save(hotelRequestDto.toHotelEntity());
    }

    /**
     * 도시 이름에 따라 호텔을 가져옴
     *
     * @param name 검색할 도시의 이름
     * @return 지정된 도시에 속한 호텔들의 정보를 포함한 리스트
     */
    @Override
    public List<HotelResponseDTO> getHotelByCityName(String name) {
        return hotelRepository.findHotelByCityName(name);
    }

    /**
     * 도시 이름에 따라 가격(높은 순)으로 호텔을 가져옴
     *
     * @param name 가져올 호텔 정보의 도시 이름
     * @return 가격(높은 순)으로 정렬된 해당 도시의 호텔 정보 리스트
     */
    @Override
    public List<HotelResponseDTO> getHotelByCityNameOrderByRoomCostDesc(String name) {
        return hotelRepository.findHotelByCityNameOrderByRoomCostDesc(name);
    }

    /**
     * 도시 이름에 따라 가격(낮은 순)으로 호텔을 가져옴
     *
     * @param name 가져올 호텔 정보의 도시 이름
     * @return 가격(낮은 순)으로 정렬된 해당 도시의 호텔 정보 리스트
     */
    @Override
    public List<HotelResponseDTO> getHotelByCityNameOrderByRoomCostAsc(String name) {
        return hotelRepository.findHotelByCityNameOrderByRoomCostAsc(name);
    }


    /**
     * 나라 이름에 따라 호텔을 가져옴
     *
     * @param name 가져올 호텔 정보의 나라 이름
     * @return Optional 객체의 값이 없으면 true -> null 반환, 있으면 false -> Optional로 래핑된 호텔 정보 반환
     */
    @Override
    public Optional<List<HotelResponseDTO>> getHotelByNationName(String name) {
        List<HotelResponseDTO> hotelByNationName = hotelRepository.findHotelByNationName(name);
        return hotelByNationName.isEmpty() ? Optional.empty() : Optional.of(hotelByNationName);
    }

    // Optional
    // Optional 객체의 상태를 확인하는 메소드 isPresent(), ifPresent(), isEmpty()
    // isPresent(): Optional 객체의 값이 있으면 true 반환, 없으면 false 반환
    // isEmpty(): Optional 객체의 값이 없으면 true 반환, 있으면 false 반환
    // ifPresent(): Optional 객체의 값이 있으면 수행문을 수행

    // orElse(): Optional 객체에 값이 있으면 그 값을 반환하고, 값이 없으면 매개변수(인자)로 제공된 기본 값을 반환
    // orElseThrow(): Optional 객체에 값이 있으면 그 값을 반환하고, 값이 없으면 예외를 던진다.
    // map(): Optional 객체에 값이 있으면 그 값을 매핑 함수에 적용하고, 없으면 빈 Optional을 반환
    // empty(): Optional 객체를 비어 있는 상태로 생성하는 정적 메서드
    // 이는 값이 없는 비어있는 Optional을 반환합니다. 이를 통해 null 대신 Optional.empty()를 사용할 수 있고, 값이 없음을 명확하게 명시

    // Optional.of(): .of는 null이 아닌 값을 래핑, null 값을 전달하면 NullPointerException이 발생
    // Optional.ofNullable(): null일 수 있는 값을 래핑, null 값을 전달하면 Optional.empty()를 반환

    // Optional<List<T>>: 리스트가 존재할 수도 있고, 존재하지 않을 수도 있는 경우를 표현
    // List<Optional<T>>: 리스트 자체는 항상 존재하지만, 리스트 안의 각 요소가 있을 수도 있고, 없을 수도 있는 경우를 표현
}
