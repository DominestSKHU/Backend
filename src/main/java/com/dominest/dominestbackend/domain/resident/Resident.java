package com.dominest.dominestbackend.domain.resident;

import com.dominest.dominestbackend.domain.common.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Resident extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    private int grade; // 학년
    // 엑셀데이터는 6자리로 저장되긴 하는데, 날짜 필터링 걸려면 날짜타입 사용해야 할 듯
    // Todo: 날짜타입 유지하면서 6자리로 표시할 수 있는지?
    @Column(nullable = false)
    private LocalDate dateOfBirth;

    @Column(nullable = false)
    private int semester; // 차수
    @Column(nullable = false)
    private String currentStatus;  // 현재상태,   Todo: 'A', 'I' 말고 다른 상태가 있는지 확인필요
    @Column(nullable = false)
    private String dormitory;
    private String period; // 기간. 'LY' 'AY' 'VY' 'YY'
    private String roomNumber; // 호실
    private String assignedRoom; // 배정방. 'B1049A' 와 같음

    // 엑셀데이터는 6자리로 저장되긴 하는데, 날짜 필터링 걸려면 날짜타입 사용해야 할 듯
    // Todo: 날짜타입 유지하면서 6자리로 표시할 수 있는지?
    private LocalDate admissionDate; // 입사일
    private LocalDate leavingDate; // 퇴사일
    private LocalDate semesterStartDate; // 학기시작일
    private LocalDate semesterEndDate; // 학기종료일

    private String phoneNumber; // '010-1234-5678' 형식으로 저장

    private String socialCode; // 사회코드
    private String socialName; // 사회명

    private String zipCode; // 우편번호
    private String address; // 주소. 이거 거주지별로 분류할 일이 생기나?
}

