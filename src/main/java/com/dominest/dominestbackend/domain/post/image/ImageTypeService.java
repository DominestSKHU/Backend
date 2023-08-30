package com.dominest.dominestbackend.domain.post.image;

import com.dominest.dominestbackend.api.post.image.dto.SaveImageTypeDto;
import com.dominest.dominestbackend.domain.post.component.category.Category;
import com.dominest.dominestbackend.domain.post.component.category.component.Type;
import com.dominest.dominestbackend.domain.post.component.category.service.CategoryService;
import com.dominest.dominestbackend.domain.user.User;
import com.dominest.dominestbackend.domain.user.service.UserService;
import com.dominest.dominestbackend.global.exception.ErrorCode;
import com.dominest.dominestbackend.global.exception.exceptions.BusinessException;
import com.dominest.dominestbackend.global.util.EntityUtil;
import com.dominest.dominestbackend.global.util.FileService;
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
    private final CategoryService categoryService;
    private final FileService fileService;

    @Transactional
    public Long create(SaveImageTypeDto.Req reqDto
                                    , Long categoryId, String uploaderEmail) {
        Category category = categoryService.getCategoryById(categoryId);
        if (! Type.IMAGE.equals(category.getType())) // 이미지 게시물이 작성될 카테고리의 타입 검사
            throw new BusinessException(ErrorCode.CATEGORY_TYPE_MISMATCHED);

        User writer = userService.getUserByEmail(uploaderEmail);

        List<String> savedImgUrls = fileService.save(FileService.FilePrefix.POST_IMAGE_TYPE, reqDto.getPostImages());
        ImageType imageType = reqDto.toEntity(savedImgUrls, writer, category);
        return imageTypeRepository.save(imageType).getId();
    }

    public ImageType getById(Long imageTypeId) {
        return EntityUtil.mustNotNull(imageTypeRepository.findByIdFetchWriterAndImageUrls(imageTypeId), ErrorCode.POST_NOT_FOUND);
    }

    public Page<ImageType> getPage(Long categoryId, Pageable pageable) {
        return imageTypeRepository.findAllFetchWriter(categoryId, pageable);
    }

    @Transactional
    public long update(SaveImageTypeDto.Req reqDto, Long imageTypeId) {
        ImageType imageType = EntityUtil.mustNotNull(imageTypeRepository.findById(imageTypeId), ErrorCode.POST_NOT_FOUND);

        List<String> savedImgUrls = fileService.save(FileService.FilePrefix.POST_IMAGE_TYPE, reqDto.getPostImages());
        imageType.setImageUrls(savedImgUrls);
        return imageType.getId();
    }

    @Transactional
    public ImageType deleteById(Long imageTypeId) {
        ImageType imageType = EntityUtil.mustNotNull(imageTypeRepository.findByIdFetchImageUrls(imageTypeId), ErrorCode.POST_NOT_FOUND);
        imageTypeRepository.delete(imageType);

        return imageType;
    }
}











