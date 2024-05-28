package com.namuk.gadget.dto.hotel;

import com.namuk.gadget.domain.Hotel;
import lombok.Data;

@Data
public class HotelRequestDTO {

    private String name;

    private String phone;

    private String email;

    private String site;

    private double latitude;

    private double longitude;

    private Long type;

    private String address;

    public Hotel toHotelEntity() {
        return Hotel.builder()
                .hotelName(this.name)
                .hotelPhone(this.phone)
                .hotelEmail(this.email)
                .hotelSite(this.site)
                .hotelLatitude(this.latitude)
                .hotelLongitude(this.longitude)
                .hotelType(this.type)
                .hotelAddress(this.address)
                .build();
    }
}
