package com.dominest.dominestbackend.domain.post.complaint;

import com.dominest.dominestbackend.api.post.complaint.dto.CreateComplaintDto;
import com.dominest.dominestbackend.api.post.complaint.dto.UpdateComplaintDto;
import com.dominest.dominestbackend.domain.post.common.RecentPost;
import com.dominest.dominestbackend.domain.post.common.RecentPostService;
import com.dominest.dominestbackend.domain.post.component.category.Category;
import com.dominest.dominestbackend.domain.post.component.category.component.Type;
import com.dominest.dominestbackend.domain.post.component.category.service.CategoryService;
import com.dominest.dominestbackend.domain.user.User;
import com.dominest.dominestbackend.domain.user.service.UserService;
import com.dominest.dominestbackend.global.exception.ErrorCode;
import com.dominest.dominestbackend.global.util.EntityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ComplaintService {
    private final ComplaintRepository complaintRepository;
    private final UserService userService;
    private final CategoryService categoryService;
    private final RecentPostService recentPostService;

    @Transactional
    public long create(CreateComplaintDto.Req reqDto, Long categoryId, String email) {
        // Complaint 연관 객체인 category, user 찾기
        User user = userService.getUserByEmail(email);
        // Complaint 연관 객체인 category 찾기
        Category category = categoryService.validateCategoryType(categoryId, Type.COMPLAINT);

        Complaint complaint = reqDto.toEntity(user, category);


        Complaint compl = complaintRepository.save(complaint);
        RecentPost recentPost = RecentPost.builder()
                .title(compl.getRoomNo() + "호 민원 기록")
                .categoryLink(compl.getCategory().getPostsLink())
                .categoryType(compl.getCategory().getType())
                .link(null)
                .build();
        recentPostService.create(recentPost);

        // Complaint 객체 생성 후 저장
        return compl.getId();
    }

    @Transactional
    public long update(Long complaintId, UpdateComplaintDto.Req reqDto) {
        Complaint complaint = getById(complaintId);

        complaint.updateValues(
                reqDto.getName()
                , reqDto.getRoomNo()
                , reqDto.getComplaintCause()
                , reqDto.getComplaintResolution()
                , reqDto.getProcessState()
                , reqDto.getDate()
        );
        return complaint.getId();
    }

    public Complaint getById(Long id) {
        return EntityUtil.mustNotNull(complaintRepository.findById(id), ErrorCode.COMPLAINT_NOT_FOUND);
    }

    @Transactional
    public long delete(Long id) {
        Complaint complaint = getById(id);
        complaintRepository.delete(complaint);
        return complaint.getId();
    }

    public Page<Complaint> getPage(Long categoryId, Pageable pageable, String complSchText, String roomNoSch) {
        if (StringUtils.hasText(roomNoSch)) {
            return complaintRepository.findAllByCategoryIdAndRoomNo(categoryId, roomNoSch, pageable);
        }

        if (StringUtils.hasText(complSchText)) {
            return complaintRepository.findAllByCategoryIdSearch(categoryId, complSchText + "*", pageable);
        }
        return complaintRepository.findPageAllByCategoryId(categoryId, pageable);
    }
}
















