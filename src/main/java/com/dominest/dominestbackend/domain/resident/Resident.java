package com.dominest.dominestbackend.domain.resident;

import com.dominest.dominestbackend.domain.common.BaseEntity;
import com.dominest.dominestbackend.domain.post.sanitationcheck.floor.checkedroom.CheckedRoom;
import com.dominest.dominestbackend.domain.resident.component.ResidenceSemester;
import com.dominest.dominestbackend.domain.room.Room;
import com.dominest.dominestbackend.global.util.TimeUtil;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(uniqueConstraints = {
        // unique 제약에서 '학기' 는 기본적으로 깔고 간다.
        // 1. [학번, 전화번호, 이름] 중복제한:  똑같은 학생이 한 학기에 둘 이상 있을 순 없다.
        // 2. [방번호]가 중복되면 안된다. 학기중 하나의 방, 하나의 구역에 둘 이상이 있을 순 없다.
        @UniqueConstraint(name = "unique_resident_Info_for_studentId_and_name",
                                            columnNames = { "studentId", "phoneNumber", "name" ,"residenceSemester" })
        , @UniqueConstraint(name = "unique_resident_Info_for_room",
                                            columnNames = { "room_id", "residenceSemester" })
})
public class Resident extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 학생 개인정보 */
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String gender; // 현재 'M' or 'F' 인데 확장성을 위해 String 쓰기로 함
    @Column(nullable = false)
    private String studentId;
    @Column(nullable = false)
    private String major; // 전공. 매학년 바뀔 수도 있으니 enum 사용하지 않는 걸로
    @Column(nullable = false)
    private String grade; // '3학년' 과 같은 식으로 저장된다, //todo 이거 숫자로 바꿀 생각도 해보자
    private String phoneNumber; // '010-1234-5678' 형식으로 저장
    // 엑셀데이터는 6자리로 저장되긴 하는데, 날짜 필터링 걸려면 날짜타입 사용해야 할 듯
    // Todo: 날짜타입 유지하면서 6자리로 표시할 수 있는지?
    @Column(nullable = false)
    private LocalDate dateOfBirth;

    /** 기숙사정보 */
    @Column(nullable = false)
    private String semester; // 차수. '2023SMSK02' 형식
    @Column(nullable = false)
    private String currentStatus;  // 현재상태,   Todo: 'A', 'I' 말고 다른 상태가 있는지 확인필요

    private String period; // 기간. 'LY' 'AY' 'VY' 'YY'

    // 호실(1, 2), 배정방, 기숙사(A, B) 3개가 물리적인 위치에 관여
    /** 날짜정보 */
    // 엑셀데이터는 8자리로 저장되긴 하는데, 날짜 필터링 걸려면 날짜타입 사용해야 할 듯
    // Todo: 날짜타입 유지하면서 8자리로 표시할 수 있는지?
    // 아래의 날짜들은 '20191106' 형식으로 저장됨
    private LocalDate admissionDate; // 입사일.
    @Column(nullable = true)
    private LocalDate leavingDate; // 퇴사일
    private LocalDate semesterStartDate; // 학기시작일
    private LocalDate semesterEndDate; // 학기종료일

    private String socialCode; // 사회코드
    private String socialName; // 사회명

    private String zipCode; // 우편번호
    private String address; // 주소. 이거 거주지별로 분류할 일이 생기나?

    private Integer penalty; // 벌점.

    /** 아래는 학생정보 페이지에 표시되지 않는 정보들
     *
     * */
    //ResidenceSemester를 Room에도 둘까 했으나, 여기서 조인해서 쓰면 됨.
    @Enumerated(EnumType.STRING)
    private ResidenceSemester residenceSemester; // 거주학기. '2020-2' 와 같음

    @Column(nullable = true)
    @Setter
    private String admissionPdfFileName; // UUID로 저장된다.
    @Column(nullable = true)
    @Setter
    private String departurePdfFileName; // UUID로 저장된다.

    // 방 정보는 하나지만 학생데이터는 학기마다 추가됨. N : 1
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    @OneToMany(mappedBy = "resident", fetch = FetchType.LAZY)
    private List<CheckedRoom> checkedRooms = new ArrayList<>();

    @Builder
    private Resident(String name, String gender, String studentId, String major, String grade,
                    LocalDate dateOfBirth, String semester, ResidenceSemester residenceSemester, String currentStatus, String period,
                     LocalDate admissionDate, LocalDate leavingDate,
                    LocalDate semesterStartDate, LocalDate semesterEndDate, String phoneNumber, String socialCode,
                    String socialName, String zipCode, String address, Room room) {
        this.name = name;
        this.gender = gender;
        this.studentId = studentId;
        this.major = major;
        this.grade = grade;
        this.dateOfBirth = TimeUtil.parseyyMMddToLocalDate(dateOfBirth);
        this.semester = semester;
        this.residenceSemester = residenceSemester;
        this.currentStatus = currentStatus;
        this.period = period;
        this.room = room;
        this.admissionDate = admissionDate;
        this.leavingDate = leavingDate;
        this.semesterStartDate = semesterStartDate;
        this.semesterEndDate = semesterEndDate;
        this.phoneNumber = phoneNumber;
        this.socialCode = socialCode;
        this.socialName = socialName;
        this.zipCode = zipCode;
        this.address = address;
        this.penalty = 0;
    }

    public static Resident from(List<String> data, ResidenceSemester residenceSemester, Room room) {
        String yyyyMMddPattern = "yyyyMMdd";

        // create the resident object using builder
        return Resident.builder()
                .name(data.get(0))
                .gender(data.get(1))
                .studentId(data.get(2))
                .semester(data.get(3))
                .residenceSemester(residenceSemester)
                .currentStatus(data.get(4))
                .dateOfBirth(TimeUtil.parseyyMMddToLocalDate(data.get(5)))
                .room(room)
                .major(data.get(7))
                .grade(data.get(8))
                .period(data.get(9))
                .admissionDate(LocalDate.parse(data.get(12), DateTimeFormatter.ofPattern(yyyyMMddPattern)))
                .leavingDate("".equals(data.get(13)) ?  null :
                        LocalDate.parse(data.get(13), DateTimeFormatter.ofPattern(yyyyMMddPattern)))
                .semesterStartDate(LocalDate.parse(data.get(14), DateTimeFormatter.ofPattern(yyyyMMddPattern)))
                .semesterEndDate(LocalDate.parse(data.get(15), DateTimeFormatter.ofPattern(yyyyMMddPattern)))
                .phoneNumber(data.get(16))
                .socialCode(data.get(17))
                .socialName(data.get(18))
                .zipCode(data.get(19))
                .address(data.get(20))
                .build();
    }

    // 파라미터로 받은 entity의 값을 모두 복사해서 업데이트한다.
    public void updateValueFrom(Resident resident) {
        this.name = resident.getName();
        this.gender = resident.getGender();
        this.studentId = resident.getStudentId();
        this.major = resident.getMajor();
        this.grade = resident.getGrade();
        this.dateOfBirth = resident.getDateOfBirth();
        this.semester = resident.getSemester();
        this.residenceSemester = resident.getResidenceSemester();
        this.currentStatus = resident.getCurrentStatus();
        this.room = resident.getRoom();
        this.period = resident.getPeriod();
        this.admissionDate = resident.getAdmissionDate();
        this.leavingDate = resident.getLeavingDate();
        this.semesterStartDate = resident.getSemesterStartDate();
        this.semesterEndDate = resident.getSemesterEndDate();
        this.phoneNumber = resident.getPhoneNumber();
        this.socialCode = resident.getSocialCode();
        this.socialName = resident.getSocialName();
        this.zipCode = resident.getZipCode();
        this.address = resident.getAddress();
    }

    public void increasePenalty(int penalty) {
        this.penalty += penalty;
    }

    public void changePenalty(Integer oldPenalty, int newPenalty) {
        this.penalty -= oldPenalty;
        this.penalty += newPenalty;
    }
}

