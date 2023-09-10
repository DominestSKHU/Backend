package com.dominest.dominestbackend.domain.post.cardkey;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CardKeyService {
    private final CardKeyRepository cardKeyRepository;
}
