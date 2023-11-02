package com.ssafy.realrealfinal.pixelms.api.pixel.service;

import com.ssafy.realrealfinal.pixelms.api.pixel.response.CreditRes;
import com.ssafy.realrealfinal.pixelms.api.pixel.response.PixelInfoRes;

import java.util.List;

public interface PixelService {

    void updateUsedPixel(Integer providerId);

    Integer getAvailableCredit(Integer providerId);

    void updatePixelRedisAndSendRank(Integer providerId, List pixelInfo);

    CreditRes updateAndSendCredit(Object object);

    PixelInfoRes getUrlAndName(String index);
}
