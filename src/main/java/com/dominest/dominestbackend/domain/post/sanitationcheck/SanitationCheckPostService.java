package com.dominest.dominestbackend.domain.post.sanitationcheck;

import com.dominest.dominestbackend.domain.post.common.RecentPost;
import com.dominest.dominestbackend.domain.post.common.RecentPostService;
import com.dominest.dominestbackend.domain.post.component.category.Category;
import com.dominest.dominestbackend.domain.post.component.category.component.Type;
import com.dominest.dominestbackend.domain.post.component.category.service.CategoryService;
import com.dominest.dominestbackend.domain.post.sanitationcheck.floor.Floor;
import com.dominest.dominestbackend.domain.post.sanitationcheck.floor.FloorService;
import com.dominest.dominestbackend.domain.post.sanitationcheck.floor.checkedroom.CheckedRoom;
import com.dominest.dominestbackend.domain.post.sanitationcheck.floor.checkedroom.CheckedRoomService;
import com.dominest.dominestbackend.domain.post.sanitationcheck.floor.checkedroom.component.ResidentInfo;
import com.dominest.dominestbackend.domain.resident.Resident;
import com.dominest.dominestbackend.domain.resident.ResidentRepository;
import com.dominest.dominestbackend.domain.resident.component.ResidenceSemester;
import com.dominest.dominestbackend.domain.room.Room;
import com.dominest.dominestbackend.domain.room.RoomService;
import com.dominest.dominestbackend.domain.user.User;
import com.dominest.dominestbackend.domain.user.service.UserService;
import com.dominest.dominestbackend.global.exception.ErrorCode;
import com.dominest.dominestbackend.global.util.EntityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class SanitationCheckPostService {
    private final SanitationCheckPostRepository sanitationCheckPostRepository;
    private final UserService userService;
    private final CategoryService categoryService;
    private final CheckedRoomService checkedRoomService;
    private final FloorService floorService;
    private final RoomService roomService;
    private final ResidentRepository residentRepository;
    private final RecentPostService recentPostService;

    public SanitationCheckPost getById(Long id) {
        return EntityUtil.mustNotNull(sanitationCheckPostRepository.findById(id), ErrorCode.POST_NOT_FOUND);
    }
    /**
     * 방역점검 게시글 생성한다.
     * 게시글만 생성하는 것이 아니라, 게시글에 연관된 각 층수(Floor 객체),
     * 각 층수에 연관된 CheckedRoom 객체도 생성한다.
     * DB에 많은 데이터를 삽입하는 작업.
     *
     * @return 생성된 게시글의 id
     */
    @Transactional
    public long create(@NotNull(message = "학기를 선택해주세요.") ResidenceSemester residenceSemester, Long categoryId, String email) {
        // sani...Post 연관 객체인 category, user 찾기
        User user = userService.getUserByEmail(email);
        // sani...Post 연관 객체인 category 찾기
        Category category = categoryService.validateCategoryType(
                categoryId, Type.SANITATION_CHECK);

        // sani...Post 객체 생성
        SanitationCheckPost saniChkPost = SanitationCheckPost.builder()
                .titleWithCurrentDate(createTitle())
                .category(category)
                .writer(user)
                .residenceSemester(residenceSemester)
                .build();
        sanitationCheckPostRepository.save(saniChkPost);

        // Floor 객체 생성
        final int START_FLOOR_NO = 2;
        final int END_FLOOR_NO = 10;
        List<Floor> floors = new ArrayList<>();

        for (int i = START_FLOOR_NO; i <= END_FLOOR_NO; i++) {
            Floor floor = Floor.builder()
                    .floorNumber(i)
                    .sanitationCheckPost(saniChkPost)
                    .build();
            floors.add(floor);
        }

        floors = floorService.create(floors);

        // CheckedRoom 객체 생성. 저장시 Room 객체와 Floor객체가 필요함.
        ArrayList<CheckedRoom> checkedRooms = new ArrayList<>();
        for (Floor floor : floors) {
            Integer floorNumber = floor.getFloorNumber();
            List<Room> rooms = roomService.getByFloorNo(floorNumber);
            for (Room room : rooms) { // CheckedRoom 은 Room 만큼 생성되어야 한다.
                ResidentInfo residentInfo = residentRepository.findByResidenceSemesterAndRoom(residenceSemester, room)
                        .map(ResidentInfo::from)
                        .orElse(null);

                CheckedRoom checkedRoom = CheckedRoom.builder()
                        .room(room)
                        .floor(floor)
                        .passState(CheckedRoom.PassState.NOT_PASSED)
                        .residentInfo(residentInfo) // null이든 아니든 그냥 저장.
                        .build();
                checkedRooms.add(checkedRoom);
            }
        }
        checkedRoomService.create(checkedRooms);

        RecentPost recentPost = RecentPost.builder()
                .title(saniChkPost.getTitle())
                .categoryLink(saniChkPost.getCategory().getPostsLink())
                .categoryType(saniChkPost.getCategory().getType())
                .link("/posts/sanitation-check/" + saniChkPost.getId() + "/floors")
                .build();
        recentPostService.create(recentPost);

        return saniChkPost.getId();
    }

    @Transactional
    public void delete(Long postId) {
        SanitationCheckPost post = getById(postId);
        sanitationCheckPostRepository.delete(post);
    }

    @Transactional
    public long updateTitle(Long postId, String title) {
        SanitationCheckPost saniCheckPost = getById(postId);
        saniCheckPost.setTitle(title);
        return postId;
    }

    public Page<SanitationCheckPost> getPage(Long categoryId, Pageable pageable) {
        return sanitationCheckPostRepository.findAllByCategory(categoryId, pageable);
    }

    public SanitationCheckPost getByIdFetchCategory(Long postId) {
        return EntityUtil.mustNotNull(sanitationCheckPostRepository.findByIdFetchCategory(postId), ErrorCode.POST_NOT_FOUND);
    }

    private String createTitle() {
        // 원하는 형식의 문자열로 변환
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = LocalDateTime.now().format(formatter);
        return formattedDate + "방역호실점검";
    }
}















