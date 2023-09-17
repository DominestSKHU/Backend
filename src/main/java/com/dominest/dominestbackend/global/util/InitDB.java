package com.dominest.dominestbackend.global.util;


import com.dominest.dominestbackend.domain.post.component.category.Category;
import com.dominest.dominestbackend.domain.post.component.category.component.Type;
import com.dominest.dominestbackend.domain.post.component.category.repository.CategoryRepository;
import com.dominest.dominestbackend.domain.room.Room;
import com.dominest.dominestbackend.domain.room.RoomRepository;
import com.dominest.dominestbackend.domain.user.User;
import com.dominest.dominestbackend.domain.user.component.Role;
import com.dominest.dominestbackend.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Component
public class InitDB {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final RoomRepository roomRepository;

    @Transactional
    @PostConstruct
    public void init() {
        ArrayList<User> users = new ArrayList<>();
        User user1sj = User.builder()
                .email("shku1")
                .password(passwordEncoder.encode("shku1"))
                .name("실장님")
                .phoneNumber("1")
                .role(Role.ROLE_ADMIN)
                .build();
        User user2sg = User.builder()
                .email("skhu2")
                .password(passwordEncoder.encode("skhu2"))
                .name("사감님")
                .phoneNumber("2")
                .role(Role.ROLE_ADMIN)
                .build();
        User user3bs = User.builder()
                .email("skhu3")
                .password(passwordEncoder.encode("skhu3"))
                .name("반병선")
                .phoneNumber("3")
                .role(Role.ROLE_ADMIN)
                .build();
        User user4ji = User.builder()
                .email("skhu4")
                .password(passwordEncoder.encode("skhu4"))
                .name("정인")
                .phoneNumber("4")
                .role(Role.ROLE_ADMIN)
                .build();
        User user5de = User.builder()
                .email("skhu5")
                .password(passwordEncoder.encode("skhu5"))
                .name("안다은")
                .phoneNumber("5")
                .role(Role.ROLE_ADMIN)
                .build();
        User user6cl = User.builder()
                .email("skhu6")
                .password(passwordEncoder.encode("skhu6"))
                .name("류채림")
                .phoneNumber("6")
                .role(Role.ROLE_ADMIN)
                .build();
        User user7cw = User.builder()
                .email("skhu7")
                .password(passwordEncoder.encode("skhu7"))
                .name("공채원")
                .phoneNumber("7")
                .role(Role.ROLE_ADMIN)
                .build();
        User user8hn = User.builder()
                .email("skhu8")
                .password(passwordEncoder.encode("skhu8"))
                .name("윤하늘")
                .phoneNumber("8")
                .role(Role.ROLE_ADMIN)
                .build();
        users.add(user1sj);
        users.add(user2sg);
        users.add(user3bs);
        users.add(user4ji);
        users.add(user5de);
        users.add(user6cl);
        users.add(user7cw);
        users.add(user8hn);
        userRepository.saveAll(users);



        Category undelivCategoryNo1 = Category.builder()
                .name("장기 미수령 택배 관리대장")
                .type(Type.UNDELIVERED_PARCEL_REGISTER)
                .explanation("장기 미수령 택배 관리대장")
                .orderKey(1)
                .build();
        categoryRepository.save(undelivCategoryNo1);

        Category complaintCategoryNO2 = Category.builder()
                .name("민원접수내역")
                .type(Type.COMPLAINT)
                .explanation("민원접수내역")
                .orderKey(2)
                .build();
        categoryRepository.save(complaintCategoryNO2);

        Category cardKeyCategoryNO3 = Category.builder()
                .name("카드키 관리대장")
                .type(Type.CARD_KEY)
                .explanation("카드키 관리대장")
                .orderKey(3)
                .build();
        categoryRepository.save(cardKeyCategoryNO3);

        Category sanitationCheckCategoryNO4 = Category.builder()
                .name("방역호실점검")
                .type(Type.SANITATION_CHECK)
                .explanation("방역호실점검")
                .orderKey(4)
                .build();
        categoryRepository.save(sanitationCheckCategoryNO4);

        Category imageCategoryNo5 = Category.builder()
                .name("사진 업로드")
                .type(Type.IMAGE)
                .explanation("사진 업로드")
                .orderKey(5)
                .build();
        categoryRepository.save(imageCategoryNo5);
        
//        UndeliveredParcelPost unDeliParcelPost = UndeliveredParcelPost.builder()
//                .titleWithCurrentDate(createTitle())
//                .category(undelivCategoryNo1)
//                .writer(user1sj)
//                .build();
//        undelivParcelPostRepository.save(unDeliParcelPost);
//
//        UndeliveredParcel parcel = UndeliveredParcel.builder()
//                .recipientName("받는사람")
//                .recipientPhoneNum("010-1234-5678")
//                .instruction("배송지시사항")
//                .processState(UndeliveredParcel.ProcessState.MESSAGE_SENT)
//                .post(unDeliParcelPost)
//                .build();
//        UndeliveredParcel parcel2 = UndeliveredParcel.builder()
//                .recipientName("받는사람")
//                .recipientPhoneNum("010-1234-5678")
//                .instruction("배송지시사항")
//                .processState(UndeliveredParcel.ProcessState.MESSAGE_SENT)
//                .post(unDeliParcelPost)
//                .build();
//        undelivParcelRepository.save(parcel);
//        undelivParcelRepository.save(parcel2);


//        ArrayList<Category> categories = new ArrayList<>();
//        int categoryCount = 7;
//        for (int i = 1; i <= categoryCount; i++) {
//            Category category = Category.builder()
//                    .name("categoryName" + i)
//                    .type(Type.IMAGE)
//                    .explanation("explanation")
//                    .orderKey(categoryCount + i)
//                    .build();
//            categories.add(category);
//        }
//        categoryRepository.saveAll(categories);
//
//
//        ArrayList<ImageType> imageTypes = new ArrayList<>();
//        int postCount = 100;
//        for (int i = 1; i <= postCount; i++) {
//            ImageType imageType = ImageType.builder()
//                    .title("title" + i)
//                    .writer(user1sj)
//                    .category(categories.get(2)) // 3번째 카테고리
//                    .build();
//            imageTypes.add(imageType);
//        }
//        imageTypeRepository.saveAll(imageTypes);
//
//        ArrayList<Favorite> favorites = new ArrayList<>();
//        for (int i = 1; i <= categoryCount; i++) {
//            Favorite favorite = Favorite.builder()
//                    .user(user1sj)
//                    .category(categories.get(i - 1))
//                    .build();
//            favorites.add(favorite);
//        }
//        favoriteRepository.saveAll(favorites);
//
//        ArrayList<Complaint> complaints = new ArrayList<>();
//        int complaintCount = 23;
//        for (int i = 1; i <= complaintCount; i++) {
//            Complaint complaint = Complaint.builder()
//                    .name("고세구먼트" + i)
//                    .roomNo("101")
//                    .complaintCause("난방 불가")
//                    .complaintResolution("난방 수으리 완무료")
//                    .processState(Complaint.ProcessState.PROCESSING)
//                    .date(LocalDate.now())
//                    .writer(user1sj)
//                    .category(complaintCategoryNO2) // 민원접수내역
//                    .build();
//            complaints.add(complaint);
//        }
//        complaintRepository.saveAll(complaints);
//
//        ArrayList<CardKey> cardKeys = new ArrayList<>();
//        int cardKeyCount = 23;
//        for (int i = 1; i <= cardKeyCount; i++) {
//            CardKey cardKey = CardKey.builder()
//                    .issuedDate(LocalDate.now())
//                    .roomNo("10" + i)
//                    .name("송승헌" + i)
//                    .dateOfBirth(LocalDate.of(1999, 1, i))
//                    .reIssueCnt(i)
//                    .etc(i + "번 안아줘요")
//                    .writer(user1sj)
//                    .category(cardKeyCategoryNO3)
//                    .build();
//            cardKeys.add(cardKey);
//        }
//        cardKeyRepository.saveAll(cardKeys);

        // 2~3층은 26호실까지, 4~10층은 17호실까지 있음
        List<Room> rooms = createRooms();

        roomRepository.saveAll(rooms);
    }

    private static List<Room> createRooms() {
        int roomCount2To3 = 26;
        int roomCount4To10 = 17;
        List<Room> rooms = new ArrayList<>();

        // 2층
        createRoomsFor(roomCount2To3, "02",rooms);
        createRoomsFor(roomCount2To3, "03",rooms);

        // 4~10층
        createRoomsFor(roomCount4To10, "04",rooms);
        createRoomsFor(roomCount4To10, "05",rooms);
        createRoomsFor(roomCount4To10, "06",rooms);
        createRoomsFor(roomCount4To10, "07",rooms);
        createRoomsFor(roomCount4To10, "08",rooms);
        createRoomsFor(roomCount4To10, "09",rooms);
        createRoomsFor(roomCount4To10, "10",rooms);

        return rooms;
    }

    private static void createRoomsFor(int roomCount, String floor, List<Room> rooms) {
        for (int i = 1; i <= roomCount; i++) {
            String roomNo = String.format("%02d", i);
            Integer floorNo = Integer.valueOf(floor);
            StringBuilder sb = new StringBuilder();

            Room roomA = Room.builder()
                    .assignedRoom(
                            sb.append("B")
                                    .append(floor)
                                    .append(roomNo)
                                    .append("A")
                                    .toString())
                    .floorNo(floorNo)

                    .dormitory("B")
                    .roomNo(2)
                    .build();
            rooms.add(roomA);
            sb.setLength(0);

            Room roomB = Room.builder()
                    .assignedRoom(sb
                            .append("B")
                            .append(floor)
                            .append(roomNo)
                            .append("B")
                            .toString())
                    .floorNo(floorNo)
                    .dormitory("B")
                    .roomNo(2)
                    .build();

            rooms.add(roomB);
        }
    }

    private String createTitle() {
        // 원하는 형식의 문자열로 변환
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = LocalDateTime.now().format(formatter);
        return formattedDate + " 장기미수령 택배";
    }
}
