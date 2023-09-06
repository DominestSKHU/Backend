package com.dominest.dominestbackend.domain.post.complaint;

import com.dominest.dominestbackend.api.post.complaint.dto.CreateComplaintDto;
import com.dominest.dominestbackend.domain.post.component.category.Category;
import com.dominest.dominestbackend.domain.post.component.category.component.Type;
import com.dominest.dominestbackend.domain.post.component.category.service.CategoryService;
import com.dominest.dominestbackend.domain.user.User;
import com.dominest.dominestbackend.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ComplaintService {
    private final ComplaintRepository complaintRepository;
    private final UserService userService;
    private final CategoryService categoryService;

    @Transactional
    public long create(CreateComplaintDto.Req reqDto, Long categoryId, String email) {
        // Complaint 연관 객체인 category, user 찾기
        User user = userService.getUserByEmail(email);
        // Complaint 연관 객체인 category 찾기
        Category category = categoryService.validateCategoryType(categoryId, Type.COMPLAINT);

        Complaint complaint = reqDto.toEntity(user, category);

        // Complaint 객체 생성 후 저장
        return complaintRepository.save(complaint).getId();
    }
}
