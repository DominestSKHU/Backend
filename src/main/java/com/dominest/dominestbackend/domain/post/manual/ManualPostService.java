package com.dominest.dominestbackend.domain.post.manual;

import com.dominest.dominestbackend.api.post.manual.dto.CreateManualPostDto;
import com.dominest.dominestbackend.api.post.manual.dto.UpdateManualPostDto;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

import static com.dominest.dominestbackend.global.util.FileService.FilePrefix.*;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ManualPostService {
    private final CategoryService categoryService;
    private final UserService userService;
    private final ManualPostRepository manualPostRepository;
    private final FileService fileService;

    private static final String filePathPrefix = "manual/";
    private static final String filePathSuffix = "/";

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

        Long manualPostId = manualPostRepository.save(manualPost).getId();
        saveFile(reqDto.getAttachFiles(), reqDto.getImageFiles(), reqDto.getVideoFIles(), manualPost, manualPostId);

        return manualPostId;
    }

    private void saveFile(List<MultipartFile>attachFiles, List<MultipartFile> imageFiles,
                               List<MultipartFile> videoFiles, ManualPost manualPost, Long manualPostId) {

        String subPath = filePathPrefix+manualPostId+filePathSuffix;
        List<String> savedAttachUrls = fileService.save(FileService.FilePrefix.ATTACH_TYPE, subPath, attachFiles);
        List<String> savedImgUrls = fileService.save(FileService.FilePrefix.IMAGE_TYPE, subPath, imageFiles);
        List<String> savedVideoUrls = fileService.save(FileService.FilePrefix.VIDEO_TYPE, subPath, videoFiles);
        manualPost.setAttachmentNames(savedAttachUrls, savedImgUrls, savedVideoUrls);
    }

    private void deleteFile(List<String> toDeleteAttachFileUrls, List<String> toDeleteImageFileUrls,
                            List<String> toDeleteVideoFileUrls) {
        if(toDeleteImageFileUrls!= null)
            toDeleteImageFileUrls.forEach(fileService::deleteFile);
        if(toDeleteVideoFileUrls != null)
            toDeleteVideoFileUrls.forEach(fileService::deleteFile);
        if(toDeleteAttachFileUrls != null)
            toDeleteAttachFileUrls.forEach(fileService::deleteFile);
    }

    public Page<ManualPost> getPage(Long categoryId, Pageable pageable) {
        // 카테고리 내 게시글이 1건도 없는 경우도 있으므로, 게시글과 함께 카테고리를 Join해서 데이터를 찾아오지 않는다.
        return manualPostRepository.findAllByCategory(categoryId, pageable);
    }

    public ManualPost getById(Long manualPostId) {
        return EntityUtil.mustNotNull(manualPostRepository.findById(manualPostId), ErrorCode.POST_NOT_FOUND);
    }

    public ManualPost getByIdIncludeAllColumn(Long manualPostId) {
        return EntityUtil.mustNotNull(manualPostRepository.findManualPostIncludeAllColumn(manualPostId), ErrorCode.POST_NOT_FOUND);
    }
    @Transactional
    public long delete(Long manualPostId) {
        ManualPost post = getById(manualPostId);
        manualPostRepository.delete(post);
        String filePath = filePathPrefix+manualPostId+filePathSuffix;
        fileService.deleteFile(filePath);
        return post.getId();
    }

    @Transactional
    public long update(Long manualPostId, UpdateManualPostDto.Req reqDto) {
        ManualPost manualPost = getById(manualPostId);

        //게시글 업데이트
        Optional.ofNullable(reqDto.getTitle())
                .ifPresent(manualPost::setTitle);

        Optional.ofNullable(reqDto.getHtmlContent())
                .ifPresent(manualPost::setHtmlContent);

        String subPath = filePathPrefix+manualPostId+filePathSuffix;

        Optional.ofNullable(reqDto.getAttachFiles())
                .ifPresent(attachFiles -> {
                    List<String> savedAttachUrls = fileService.save(ATTACH_TYPE, subPath, attachFiles);
                    manualPost.addAttachmentUrls(savedAttachUrls);
                });

        Optional.ofNullable(reqDto.getImageFiles())
                .ifPresent(imageFiles -> {
                    List<String> savedImageUrls = fileService.save(IMAGE_TYPE, subPath, imageFiles);
                    manualPost.addImageUrls(savedImageUrls);
                });

        Optional.ofNullable(reqDto.getVideoFiles())
                .ifPresent(videoFiles -> {
                    List<String> savedVideoUrls = fileService.save(VIDEO_TYPE, subPath, videoFiles);
                    manualPost.addVideoUrls(savedVideoUrls);
                });

        //수정으로 인해 필요 없어진 파일들 삭제
        List<String> toDeleteAttachmentUrls = reqDto.getToDeleteAttachUrls();
        List<String> toDeleteImageUrls = reqDto.getToDeleteImageUrls();
        List<String> toDeleteVideoUrls = reqDto.getToDeleteVideoUrls();

        deleteFile(toDeleteAttachmentUrls , toDeleteImageUrls, toDeleteVideoUrls);
        manualPost.deleteUrls(toDeleteAttachmentUrls , toDeleteImageUrls, toDeleteVideoUrls);

        return manualPostRepository.save(manualPost).getId();
    }
}