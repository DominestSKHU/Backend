package com.dominest.dominestbackend.domain.post.image;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ImageTypeService {
    private final ImageTypeRepository imageTypeRepository;
}
