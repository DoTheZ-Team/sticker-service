package com.justdo.glue.sticker.domain.sticker.dto;

import com.justdo.glue.sticker.domain.sticker.Sticker;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

public class StickerResponse {
    @Schema(description = "스티커 생성 요청 응답 DTO")
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class StickerGenerationResult {

        @Schema(description = "사용자가 작성한 프롬프트")
        private String stickerPrompt;

        @Schema(description = "스티커 타입(사진 또는 스티커형식)")
        private String stickerType;

        @Schema(description = "정확히는 url이 아닌 base64 형태로 저장")
        private String stickerUrl;

    }

    public static StickerGenerationResult toStickerGenerationResult(String stickerPrompt, String stickerType, String stickerUrl) {

        return StickerGenerationResult.builder()
                .stickerPrompt(stickerPrompt)
                .stickerType(stickerType)
                .stickerUrl(stickerUrl)
                .build();
    }

    @Schema(description = "스티커 정보 DTO")
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class StickerItem{
        private Long stickerId;
        @Schema(description = "정확히는 url이 아닌 base64 형태로 저장")
        private String url;

    }

    public static StickerItem toSticker(Long id, String url){
        return StickerItem.builder()
                .stickerId(id)
                .url(url)
                .build();
    }
}
