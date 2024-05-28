package com.namuk.gadget.dto.hotel;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class HotelResponseDTO {

    private String hotelPictureUrl;

    private String hotelLocation;

    private String hotelName;

    private String hotelRating;

    private Long hotelReviewCount;

    private Long roomCost;

    private Long roomPeople;

    private String cityName;

    public HotelResponseDTO(String pictureUrl, String location, String name, String rating, Long reviewCount, Long cost, Long people, String cityName) {
        this.hotelPictureUrl = pictureUrl;
        this.hotelLocation = location;
        this.hotelName = name;
        this.hotelRating = rating;
        this.hotelReviewCount = reviewCount;
        this.roomCost = cost;
        this.roomPeople = people;
        this.cityName = cityName;
    }
}
