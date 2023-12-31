package com.dominest.dominestbackend.domain.post.image;

import com.dominest.dominestbackend.api.post.image.dto.SaveImageTypeDto;
import com.dominest.dominestbackend.domain.post.common.RecentPost;
import com.dominest.dominestbackend.domain.post.common.RecentPostService;
import com.dominest.dominestbackend.domain.post.component.category.Category;
import com.dominest.dominestbackend.domain.post.component.category.component.Type;
import com.dominest.dominestbackend.domain.post.component.category.service.CategoryService;
import com.dominest.dominestbackend.domain.user.User;
import com.dominest.dominestbackend.domain.user.service.UserService;
import com.dominest.dominestbackend.global.exception.ErrorCode;
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
    private final RecentPostService recentPostService;

    @Transactional
    public Long create(SaveImageTypeDto.Req reqDto
                                    , Long categoryId, String uploaderEmail) {
        Category category = categoryService.getById(categoryId);
        // 이미지 게시물이 작성될 카테고리의 타입 검사
        Type.IMAGE.validateEqualTo(category.getType());

        User writer = userService.getUserByEmail(uploaderEmail);

        List<String> savedImgUrls = fileService.save(FileService.FilePrefix.POST_IMAGE_TYPE, reqDto.getPostImages());
        ImageType imageType = reqDto.toEntity(savedImgUrls, writer, category);


        ImageType saved = imageTypeRepository.save(imageType);
        RecentPost recentPost = RecentPost.builder()
                .title(saved.getTitle())
                .categoryLink(saved.getCategory().getPostsLink())
                .categoryType(saved.getCategory().getType())
                .link(saved.getCategory().getPostsLink() + "/" + saved.getId())
                .build();
        recentPostService.create(recentPost);

        return saved.getId();
    }

    public ImageType getById(Long imageTypeId) {
        return EntityUtil.mustNotNull(imageTypeRepository.findByIdFetchWriterAndImageUrls(imageTypeId), ErrorCode.POST_NOT_FOUND);
    }

    public Page<ImageType> getPage(Long categoryId, Pageable pageable) {
        return imageTypeRepository.findAllByCategory(categoryId, pageable);
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











