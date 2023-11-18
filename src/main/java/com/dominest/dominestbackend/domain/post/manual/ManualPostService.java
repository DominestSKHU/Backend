package com.dominest.dominestbackend.domain.post.manual;

import com.dominest.dominestbackend.api.post.manual.dto.CreateManualPostDto;
import com.dominest.dominestbackend.domain.post.component.category.Category;
import com.dominest.dominestbackend.domain.post.component.category.component.Type;
import com.dominest.dominestbackend.domain.post.component.category.service.CategoryService;
import com.dominest.dominestbackend.domain.user.User;
import com.dominest.dominestbackend.domain.user.service.UserService;
import com.dominest.dominestbackend.global.util.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ManualPostService {
    private final CategoryService categoryService;
    private final UserService userService;
    private final ManualPostRepository manualPostRepository;
    private final FileService fileService;

    @Transactional
    public Long create(Long categoryId, CreateManualPostDto.Req reqDto, String email) {
        Category category = categoryService.validateCategoryType(categoryId, Type.MANUAL);
        User user = userService.getUserByEmail(email);
        ManualPost manualPost = ManualPost.builder().
                title(reqDto.getTitle()).
                writer(user).
                category(category).
                htmlContent(reqDto.getHtmlContent()).
                build();
        saveFile(reqDto.getAttachFiles(), reqDto.getImageFiles(), reqDto.getVideoFIles(), manualPost);

        return manualPostRepository.save(manualPost).getId();
    }

    public void saveFile(List<MultipartFile>attachFiles, List<MultipartFile> imageFiles,
                               List<MultipartFile> videoFiles, ManualPost manualPost) {

        List<String> savedAttachUrls = fileService.save(FileService.FilePrefix.MANUAL_ATTACH_TYPE, attachFiles);
        List<String> savedImgUrls = fileService.save(FileService.FilePrefix.MANUAL_IMAGE_TYPE, imageFiles);
        List<String> savedVideoUrls = fileService.save(FileService.FilePrefix.MANUAL_VIDEO_TYPE, videoFiles);
        manualPost.setUrls(savedAttachUrls, savedImgUrls, savedVideoUrls);
    }

    public Page<ManualPost> getPage(Long categoryId, Pageable pageable) {
        // 카테고리 내 게시글이 1건도 없는 경우도 있으므로, 게시글과 함께 카테고리를 Join해서 데이터를 찾아오지 않는다.
        return manualPostRepository.findAllByCategory(categoryId, pageable);
    }
}