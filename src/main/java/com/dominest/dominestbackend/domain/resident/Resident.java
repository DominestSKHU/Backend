package com.dominest.dominestbackend.domain.resident;

import com.dominest.dominestbackend.domain.common.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Resident extends BaseTimeEntity {
    @Id
    //한 번에 많은 데이터가 insert 될 수 있으므로 SEQUENCE 사용. 근데 300개 삽입으로는 별 차이가 없네
    @SequenceGenerator(
            name = "resident_seq_generator"
            , sequenceName = "resident_seq"
            , initialValue = 1
            , allocationSize = 50)
    @GeneratedValue(strategy = GenerationType.SEQUENCE
                                    , generator = "resident_seq_generator"
    )
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String gender;
    @Column(nullable = false, unique = true)
    private String studentId;
    @Column(nullable = false)
    private String major; // 전공. 매학년 바뀔 수도 있으니 enum 사용하지 않는 걸로
    @Column(nullable = false)
    private String grade; // '3학년' 과 같은 식으로 저장된다, //todo 이거 숫자로 바꿀 생각도 해보자

    // 엑셀데이터는 6자리로 저장되긴 하는데, 날짜 필터링 걸려면 날짜타입 사용해야 할 듯
    // Todo: 날짜타입 유지하면서 6자리로 표시할 수 있는지?
    @Column(nullable = false)
    private LocalDate dateOfBirth;

    @Column(nullable = false)
    private String semester; // 차수. '2023SMSK02' 형식
    @Column(nullable = false)
    private String currentStatus;  // 현재상태,   Todo: 'A', 'I' 말고 다른 상태가 있는지 확인필요
    @Column(nullable = false)
    private String dormitory;
    private String period; // 기간. 'LY' 'AY' 'VY' 'YY'
    private Integer roomNumber; // 호실
    private String assignedRoom; // 배정방. 'B1049A' 와 같음

    /** 날짜정보 */
    // 엑셀데이터는 8자리로 저장되긴 하는데, 날짜 필터링 걸려면 날짜타입 사용해야 할 듯
    // Todo: 날짜타입 유지하면서 8자리로 표시할 수 있는지?
    // 아래의 날짜들은 '20191106' 형식으로 저장됨
    private LocalDate admissionDate; // 입사일.
    private LocalDate leavingDate; // 퇴사일
    private LocalDate semesterStartDate; // 학기시작일
    private LocalDate semesterEndDate; // 학기종료일

    private String phoneNumber; // '010-1234-5678' 형식으로 저장

    private String socialCode; // 사회코드
    private String socialName; // 사회명

    private String zipCode; // 우편번호
    private String address; // 주소. 이거 거주지별로 분류할 일이 생기나?

    @Builder
    private Resident(String name, String gender, String studentId, String major, String grade,
                    LocalDate dateOfBirth, String semester, String currentStatus, String dormitory, String period,
                     Integer roomNumber, String assignedRoom, LocalDate admissionDate, LocalDate leavingDate,
                    LocalDate semesterStartDate, LocalDate semesterEndDate, String phoneNumber, String socialCode,
                    String socialName, String zipCode, String address) {
        this.name = name;
        this.gender = gender;
        this.studentId = studentId;
        this.major = major;
        this.grade = grade;
        this.dateOfBirth = dateOfBirth;
        this.semester = semester;
        this.currentStatus = currentStatus;
        this.dormitory = dormitory;
        this.period = period;
        this.roomNumber = roomNumber;
        this.assignedRoom = assignedRoom;
        this.admissionDate = admissionDate;
        this.leavingDate = leavingDate;
        this.semesterStartDate = semesterStartDate;
        this.semesterEndDate = semesterEndDate;
        this.phoneNumber = phoneNumber;
        this.socialCode = socialCode;
        this.socialName = socialName;
        this.zipCode = zipCode;
        this.address = address;
    }

    public static Resident from(List<String> data) {
        // create the resident object using builder
        return Resident.builder()
                .name(data.get(0))
                .gender(data.get(1))
//                .studentId(data.get(2))
                .studentId(UUID.randomUUID().toString()) // todo unique 제한 때문에 임시로 UUID 사용. 윗줄 주석 나중에 풀자
                .semester(data.get(3))
                .currentStatus(data.get(4))
                .dateOfBirth(LocalDate.parse(data.get(5), DateTimeFormatter.ofPattern("yyMMdd")))
                .dormitory(data.get(6))
                .major(data.get(7))
                .grade(data.get(8))
                .period(data.get(9))
                .roomNumber(Integer.valueOf(data.get(10)))
                .assignedRoom(data.get(11))
                .admissionDate(LocalDate.parse(data.get(12), DateTimeFormatter.ofPattern("yyyyMMdd")))
                .leavingDate(LocalDate.parse(data.get(12), DateTimeFormatter.ofPattern("yyyyMMdd"))) // todo get(13)빈 값에 대응하는 코드 작성해야 함
                .semesterStartDate(LocalDate.parse(data.get(14), DateTimeFormatter.ofPattern("yyyyMMdd")))
                .semesterEndDate(LocalDate.parse(data.get(15), DateTimeFormatter.ofPattern("yyyyMMdd")))
                .phoneNumber(data.get(16))
                .socialCode(data.get(17))
                .socialName(data.get(18))
                .zipCode(data.get(19))
                .address(data.get(20))
                .build();
    }

}
