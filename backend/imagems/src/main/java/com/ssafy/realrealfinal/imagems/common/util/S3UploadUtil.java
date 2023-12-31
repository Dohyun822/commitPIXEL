package com.ssafy.realrealfinal.imagems.common.util;

import com.ssafy.realrealfinal.imagems.api.image.feignClient.PixelFeignClient;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.imageio.ImageIO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.io.ByteArrayInputStream;

@Slf4j
@RequiredArgsConstructor
@Component
public class S3UploadUtil {

    private final PixelFeignClient pixelFeignClient;
    private final AwsS3Util awsS3Util;

    /**
     * 이미지를 정각과 30분마다 Amazon S3에 업로드. 업로드될 이미지는 pixelFeignClient로 pixcelMS에게 현재 캔버스 상태 image로 요청.
     * 이미지는 "yyyy-MM-dd" 폴더에 저장. 이미지 파일의 현재 시간 (예: 12:45 -> 12.5, 00:01 -> 0.0) 이름으로 파일 저장.
     */
    @Scheduled(cron = "0 0/10 8-23,0 * * ?")
    public void S3Upload() {
        log.info("S3Upload start");
        // byte[] 형태로 이미지 받아오기
        byte[] imageBytes = pixelFeignClient.getImage();

        // byte[]를 ByteArrayInputStream으로 변환
        ByteArrayInputStream bais = new ByteArrayInputStream(imageBytes);

        // BufferedImage로 변환
        BufferedImage image = null;
        try {
            image = ImageIO.read(bais);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        LocalDateTime now = LocalDateTime.now();
        // 저장할 폴더.
        DateTimeFormatter folderFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String folderName = now.format(folderFormatter);

        // 이미지 이름
        DateTimeFormatter fileFormatter = DateTimeFormatter.ofPattern("HH.mm");
        String fileName = now.format(fileFormatter); // ".png" 확장자 제거

        awsS3Util.uploadImage(image, folderName, fileName);
        log.info("S3Upload end: SUCCESS");
    }
}
