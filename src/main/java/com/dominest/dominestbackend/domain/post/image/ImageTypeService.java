package com.dominest.dominestbackend.domain.post.image;

import com.dominest.dominestbackend.api.post.image.dto.SaveImageTypeDto;
import com.dominest.dominestbackend.domain.user.User;
import com.dominest.dominestbackend.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
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
    public void createImageType(SaveImageTypeDto.Request reqDto, List<String> imageUrls, String uploaderEmail) {
        User user = userService.getUserByEmail(uploaderEmail);
        ImageType imageType = reqDto.toEntity(imageUrls, user);
        imageTypeRepository.save(imageType);
    }
}
