package com.namuk.gadget.controller;

import com.namuk.gadget.dto.hotel.HotelResponseDTO;
import com.namuk.gadget.repository.city.CityRepository;
import com.namuk.gadget.service.hotel.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/search")
public class SearchController {

    private final HotelService hotelService;
    private final CityRepository cityRepository;

    @Autowired
    public SearchController(HotelService hotelService, CityRepository cityRepository) {
        this.hotelService = hotelService;
        this.cityRepository = cityRepository;
    }

    @GetMapping(value = "/hotel/city")
    public List<HotelResponseDTO> getHotelByCityName(@RequestParam(name = "city") String name) {
        return hotelService.getHotelByCityName(name);
    }

    /**
     * 도시 이름과 가격 정렬 유형에 따라 가져온 호텔 정보를 정렬하는 엔드포인트
     *
     * @param name                    요청한 도시 이름
     * @param sort                    정렬 유형 "price_a"는 가격(높은 순), "price_b"는 가격(낮은 순)
     * @return                        요청한 도시와 가격 정렬 조건에 맞는 호텔 정보 리스트
     * @throws IllegalAccessException 잘못된 도시 이름, 정렬 유형이 들어온 경우 발생 예외
     */
    @GetMapping(value = "/hotel/city/{sort}")
    public List<HotelResponseDTO> getHotelByCityNameOrderByRoomCost(@RequestParam(name = "city") String name,
                                                                    @PathVariable(name = "sort") String sort) throws IllegalArgumentException  {

        if(!(cityRepository.existsByCityName(name))) {
            throw new IllegalArgumentException("City not found in City Entity: " + name);
        }


        List<HotelResponseDTO> hotelCost = switch (sort) {
            case "price_a" -> hotelCost = hotelService.getHotelByCityNameOrderByRoomCostDesc(name);
            case "price_b" -> hotelCost = hotelService.getHotelByCityNameOrderByRoomCostAsc(name);
            default -> throw new IllegalArgumentException("Invalid sort: " + sort);
        };

        return hotelCost;
    }


    /**
     * 나라 이름에 따라 호텔 정보를 가져오는 엔드포인트
     * 
     * @param name 요청한 나라 이름
     * @return 요청된 나라에 맞는 호텔 정보 리스트
     * 호텔 정보 리스트 객체가 null값이라면 IllegalArgumentException예외 발생
     */
    @GetMapping(value = "/hotel/city/nation")
    public List<HotelResponseDTO> getHotelByNationName(@RequestParam(name = "nation") String name) {

        Optional<List<HotelResponseDTO>> hotelByNationName = hotelService.getHotelByNationName(name);

        return hotelByNationName.orElseThrow(() -> new IllegalArgumentException("Not found Nation"));

        // 컨트롤러 메서드에서는 Optional을 해제하여 List<HotelResponseDTO>를 반환해야 한다, 클라이언트에게 실제 데이터 리스트를 반환하기 위함이기 때문
        // 컨트롤러에서는 Optional을 사용해 값이 있는지 없는지를 확인한 후, Optional에서 값을 꺼내어 반환
    }
}
