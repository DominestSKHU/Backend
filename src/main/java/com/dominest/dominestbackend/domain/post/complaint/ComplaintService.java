package com.dominest.dominestbackend.domain.post.complaint;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ComplaintService {
    private final ComplaintRepository complaintRepository;
}
