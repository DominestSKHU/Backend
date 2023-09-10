package com.dominest.dominestbackend.global.util;


import com.dominest.dominestbackend.domain.favorite.Favorite;
import com.dominest.dominestbackend.domain.favorite.FavoriteRepository;
import com.dominest.dominestbackend.domain.post.cardkey.CardKey;
import com.dominest.dominestbackend.domain.post.cardkey.CardKeyRepository;
import com.dominest.dominestbackend.domain.post.complaint.Complaint;
import com.dominest.dominestbackend.domain.post.complaint.ComplaintRepository;
import com.dominest.dominestbackend.domain.post.component.category.Category;
import com.dominest.dominestbackend.domain.post.component.category.component.Type;
import com.dominest.dominestbackend.domain.post.component.category.repository.CategoryRepository;
import com.dominest.dominestbackend.domain.post.image.ImageType;
import com.dominest.dominestbackend.domain.post.image.ImageTypeRepository;
import com.dominest.dominestbackend.domain.post.undeliveredparcel.UndeliveredParcelPost;
import com.dominest.dominestbackend.domain.post.undeliveredparcel.UndeliveredParcelPostRepository;
import com.dominest.dominestbackend.domain.post.undeliveredparcel.component.UndeliveredParcel;
import com.dominest.dominestbackend.domain.post.undeliveredparcel.component.UndeliveredParcelRepository;
import com.dominest.dominestbackend.domain.user.User;
import com.dominest.dominestbackend.domain.user.component.Role;
import com.dominest.dominestbackend.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

@RequiredArgsConstructor
@Component
public class InitDB {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ImageTypeRepository imageTypeRepository;
    private final FavoriteRepository favoriteRepository;
    private final UndeliveredParcelPostRepository undelivParcelPostRepository;
    private final UndeliveredParcelRepository undelivParcelRepository;
    private final ComplaintRepository complaintRepository;
    private final CardKeyRepository cardKeyRepository;

    @Transactional
    @PostConstruct
    public void init() {
        final String PWD = "pppp";
        User user = User.builder()
                .email("eeee@email.com")
                .password(passwordEncoder.encode(PWD))
                .name("name1")
                .phoneNumber("010-1234-5678")
                .role(Role.ROLE_ADMIN)
                .build();
        User user2 = User.builder()
                .email("eeee1@email.com")
                .password(passwordEncoder.encode(PWD))
                .name("name2")
                .phoneNumber("010-1234-5679")
                .role(Role.ROLE_ADMIN)
                .build();
        User user3 = User.builder()
                .email("eeee2@email.com")
                .password(passwordEncoder.encode(PWD))
                .name("name3")
                .phoneNumber("010-1234-5676")
                .role(Role.ROLE_ADMIN)
                .build();
        userRepository.save(user);
        userRepository.save(user2);
        userRepository.save(user3);


        Category undelivCategoryNo1 = Category.builder()
                .name("장기 미수령 택배 관리대장1")
                .type(Type.UNDELIVERED_PARCEL_REGISTER)
                .explanation("장미택관1")
                .orderKey(1)
                .build();
        categoryRepository.save(undelivCategoryNo1);

        Category complaintCategoryNO2 = Category.builder()
                .name("민원접수내역1")
                .type(Type.COMPLAINT)
                .explanation("민접내")
                .orderKey(2)
                .build();
        categoryRepository.save(complaintCategoryNO2);

        Category cardKeyCategoryNO3 = Category.builder()
                .name("카드키관리대장1")
                .type(Type.CARD_KEY)
                .explanation("카키관1")
                .orderKey(3)
                .build();
        categoryRepository.save(cardKeyCategoryNO3);
        
        UndeliveredParcelPost unDeliParcelPost = UndeliveredParcelPost.builder()
                .titleWithCurrentDate(createTitle())
                .category(undelivCategoryNo1)
                .writer(user)
                .build();
        undelivParcelPostRepository.save(unDeliParcelPost);

        UndeliveredParcel parcel = UndeliveredParcel.builder()
                .recipientName("받는사람")
                .recipientPhoneNum("010-1234-5678")
                .instruction("배송지시사항")
                .processState(UndeliveredParcel.ProcessState.MESSAGE_SENT)
                .post(unDeliParcelPost)
                .build();
        UndeliveredParcel parcel2 = UndeliveredParcel.builder()
                .recipientName("받는사람")
                .recipientPhoneNum("010-1234-5678")
                .instruction("배송지시사항")
                .processState(UndeliveredParcel.ProcessState.MESSAGE_SENT)
                .post(unDeliParcelPost)
                .build();
        undelivParcelRepository.save(parcel);
        undelivParcelRepository.save(parcel2);


        ArrayList<Category> categories = new ArrayList<>();
        int categoryCount = 7;
        for (int i = 1; i <= categoryCount; i++) {
            Category category = Category.builder()
                    .name("categoryName" + i)
                    .type(Type.IMAGE)
                    .explanation("explanation")
                    .orderKey(categoryCount + i)
                    .build();
            categories.add(category);
        }
        categoryRepository.saveAll(categories);


        ArrayList<ImageType> imageTypes = new ArrayList<>();
        int postCount = 100;
        for (int i = 1; i <= postCount; i++) {
            ImageType imageType = ImageType.builder()
                    .title("title" + i)
                    .writer(user)
                    .category(categories.get(2)) // 3번째 카테고리
                    .build();
            imageTypes.add(imageType);
        }
        imageTypeRepository.saveAll(imageTypes);

        ArrayList<Favorite> favorites = new ArrayList<>();
        for (int i = 1; i <= categoryCount; i++) {
            Favorite favorite = Favorite.builder()
                    .user(user)
                    .category(categories.get(i - 1))
                    .build();
            favorites.add(favorite);
        }
        favoriteRepository.saveAll(favorites);

        ArrayList<Complaint> complaints = new ArrayList<>();
        int complaintCount = 23;
        for (int i = 1; i <= complaintCount; i++) {
            Complaint complaint = Complaint.builder()
                    .name("고세구먼트" + i)
                    .roomNo("101")
                    .complaintCause("난방 불가")
                    .complaintResolution("난방 수으리 완무료")
                    .processState(Complaint.ProcessState.PROCESSING)
                    .date(LocalDate.now())
                    .writer(user)
                    .category(complaintCategoryNO2) // 민원접수내역
                    .build();
            complaints.add(complaint);
        }
        complaintRepository.saveAll(complaints);

        ArrayList<CardKey> cardKeys = new ArrayList<>();
        int cardKeyCount = 23;
        for (int i = 1; i <= cardKeyCount; i++) {
            CardKey cardKey = CardKey.builder()
                    .issuedDate(LocalDate.now())
                    .roomNo("10" + i)
                    .name("송승헌" + i)
                    .dateOfBirth(LocalDate.of(1999, 1, i))
                    .reIssueCnt(i)
                    .etc(i + "번 안아줘요")
                    .writer(user)
                    .category(cardKeyCategoryNO3)
                    .build();
            cardKeys.add(cardKey);
        }
        cardKeyRepository.saveAll(cardKeys);
    }
    private String createTitle() {
        // 원하는 형식의 문자열로 변환
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = LocalDateTime.now().format(formatter);
        return formattedDate + " 장기미수령 택배";
    }
}
