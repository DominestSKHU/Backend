package com.dominest.dominestbackend.domain.post.image;

import com.dominest.dominestbackend.api.post.image.dto.ImageTypeDetailDto;
import com.dominest.dominestbackend.api.post.image.dto.ImageTypeListDto;
import com.dominest.dominestbackend.api.post.image.dto.SaveImageTypeDto;
import com.dominest.dominestbackend.domain.user.User;
import com.dominest.dominestbackend.domain.user.service.UserService;
import com.dominest.dominestbackend.global.exception.ErrorCode;
import com.dominest.dominestbackend.global.util.EntityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ImageTypeService {
    private final ImageTypeRepository imageTypeRepository;
    private final UserService userService;

    @Transactional
    public ImageType createImageType(SaveImageTypeDto.Req reqDto, List<String> imageUrls, String uploaderEmail) {
        User user = userService.getUserByEmail(uploaderEmail);
        ImageType imageType = reqDto.toEntity(imageUrls, user);
        return imageTypeRepository.save(imageType);
    }

    public ImageTypeDetailDto.Res getImageTypeById(Long imageTypeId) {
        ImageType imageType = EntityUtil.checkNotFound(imageTypeRepository.findByIdFetchWriterAndImageUrls(imageTypeId), ErrorCode.POST_NOT_FOUND);
        return ImageTypeDetailDto.Res.from(imageType);
    }

    public ImageTypeListDto.Res getImageTypes(Pageable pageable) {
        Page<ImageType> imageTypes = imageTypeRepository.findAllFetchWriter(pageable);
        return ImageTypeListDto.Res.from(imageTypes);
    }
}
