package com.justdo.glue.sticker.domain.poststicker.service;

import com.justdo.glue.sticker.domain.poststicker.PostSticker;
import com.justdo.glue.sticker.domain.sticker.dto.StickerResponse.*;
import com.justdo.glue.sticker.domain.poststicker.repository.PostStickerRepository;
import com.justdo.glue.sticker.domain.sticker.service.StickerQueryService;
import com.justdo.glue.sticker.global.exception.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.justdo.glue.sticker.domain.poststicker.dto.PostStickerDTO.*;
import static com.justdo.glue.sticker.global.response.code.status.ErrorStatus.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostStickerQueryServiceImpl implements PostStickerQueryService{

    private final PostStickerRepository postStickerRepository;
    private final StickerQueryService stickerQueryService;

    @Override
    public PostStickerItem getPostStickerById(Long id) {
        PostSticker postSticker = postStickerRepository.findById(id)
                .orElseThrow(() -> new ApiException(_STICKER_POST_NOT_FOUND));

        return toPostStickerItem(postSticker.getId(), postSticker.getPostId(), postSticker.getStickerId(), postSticker.getXLocation(), postSticker.getYLocation(), postSticker.getWidth(), postSticker.getHeight(), postSticker.getAngle());
    }

    @Override
    @Transactional
    public PostStickerItem savePostSticker(PostSticker postSticker) {
        PostSticker savedPostSticker = Optional.of(postStickerRepository.save(postSticker))
                .orElseThrow(() -> new ApiException(_STICKER_POST_NOT_SAVED));

        return toPostStickerItem(savedPostSticker.getId(), savedPostSticker.getPostId(), savedPostSticker.getStickerId(), savedPostSticker.getXLocation(), savedPostSticker.getYLocation(), savedPostSticker.getWidth(), savedPostSticker.getHeight(), savedPostSticker.getAngle());
    }

    @Override
    public PostStickerUrlItems getPostStickersByPostId(Long postId) {
        Optional<List<PostSticker>> postStickersOptional = postStickerRepository.findByPostId(postId);

        if (postStickersOptional.isEmpty()) {
            throw new ApiException(_STICKER_POST_NOT_FOUND);
        }

        List<PostSticker> postStickers = postStickersOptional.get(); // Optional에서 리스트를 추출

        List<PostStickerUrlItem> postStickerItems = postStickers.stream()
                .map(postStickeritem -> {
                    StickerItem stickerItem = stickerQueryService.getStickerById(postStickeritem.getStickerId());
                    PostStickerItem postStickerItem = toPostStickerItem(
                            postStickeritem.getId(),
                            postStickeritem.getPostId(),
                            postStickeritem.getStickerId(),
                            postStickeritem.getXLocation(),
                            postStickeritem.getYLocation(),
                            postStickeritem.getWidth(),
                            postStickeritem.getHeight(),
                            postStickeritem.getAngle()
                    );
                    return toPostStickerUrlItem(postStickerItem, stickerItem.getUrl());
                })
                .collect(Collectors.toList());


        return toPostStickerUrlItems(postStickerItems);
    }
}