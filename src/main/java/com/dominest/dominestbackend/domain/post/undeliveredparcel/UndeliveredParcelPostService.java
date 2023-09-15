package com.dominest.dominestbackend.domain.post.undeliveredparcel;

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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UndeliveredParcelPostService {
    private final UndeliveredParcelPostRepository undelivParcelPostRepository;
    private final UserService userService;
    private final CategoryService categoryService;
    private final RecentPostService recentPostService;

    @Transactional
    public Long create(Long categoryId, String email) {
        // Undeli...의 연관 객체인 category, user 찾기
        User user = userService.getUserByEmail(email);
        // Undeli...의 연관 객체인 category 찾기
        Category category = categoryService.validateCategoryType(categoryId, Type.UNDELIVERED_PARCEL_REGISTER);

        // Undeli... 객체 생성 후 저장
        UndeliveredParcelPost unDeliParcelPost = UndeliveredParcelPost.builder()
                .titleWithCurrentDate(createTitle())
                .category(category)
                .writer(user)
                .build();

        UndeliveredParcelPost post = undelivParcelPostRepository.save(unDeliParcelPost);

        RecentPost recentPost = RecentPost.builder()
                .title(post.getTitle())
                .categoryLink(post.getCategory().getPostsLink())
                .categoryType(post.getCategory().getType())
                .link("/posts/undelivered-parcel/" + post.getId())
                .build();
        recentPostService.create(recentPost);

        return post.getId();
    }

    public UndeliveredParcelPost getById(Long undelivParcelPostId) {
        return EntityUtil.mustNotNull(undelivParcelPostRepository.findById(undelivParcelPostId), ErrorCode.POST_NOT_FOUND);
    }

    public UndeliveredParcelPost getByIdFetchParcels(Long undelivParcelPostId) {
        return EntityUtil.mustNotNull(undelivParcelPostRepository.findByIdFetchParcels(undelivParcelPostId), ErrorCode.POST_NOT_FOUND);
    }

    @Transactional
    public long delete(Long undelivParcelPostId) {
        UndeliveredParcelPost post = getById(undelivParcelPostId);
        undelivParcelPostRepository.delete(post);
        return post.getId();
    }

    public Page<UndeliveredParcelPost> getPage(Long categoryId, Pageable pageable) {
        // 카테고리 내 게시글이 1건도 없는 경우도 있으므로, 게시글과 함께 카테고리를 Join해서 데이터를 찾아오지 않는다.
        return undelivParcelPostRepository.findAllByCategory(categoryId, pageable);
    }

    private String createTitle() {
        // 원하는 형식의 문자열로 변환
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = LocalDateTime.now().format(formatter);
        return formattedDate + " 장기미수령 택배";
    }
}
