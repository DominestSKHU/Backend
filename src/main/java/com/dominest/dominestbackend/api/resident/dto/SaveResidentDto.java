package com.dominest.dominestbackend.api.resident.dto;

import com.dominest.dominestbackend.domain.resident.Resident;
import com.dominest.dominestbackend.domain.resident.component.ResidenceSemester;
import com.dominest.dominestbackend.global.validation.PhoneNumber;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class SaveResidentDto {
    @NoArgsConstructor
    @Getter
    public static class Req{
        @NotBlank(message = "이름을 입력해주세요.")
        private String name;
        @NotBlank(message = "성별을 입력해주세요.")
        private String gender;
        @NotBlank(message = "학번을 입력해주세요.")
        private String studentId;
        @NotBlank(message = "학과를 입력해주세요.")
        private String major;
        @NotBlank(message = "학년을 입력해주세요.")
        private String grade;
        @NotNull(message = "생년월일을 입력해주세요.")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyMMdd")
        private LocalDate dateOfBirth;
        @NotBlank(message = "학기를 입력해주세요.")
        private String semester;
        @NotNull(message = "거주학기를 입력해주세요.")
        private ResidenceSemester residenceSemester;
        @NotBlank(message = "현재 상태를 입력해주세요.")
        private String currentStatus;
        @NotBlank(message = "기숙사를 입력해주세요.")
        private String dormitory;
        @NotBlank(message = "기간을 입력해주세요.")
        private String period;
        @Min(value = 1, message = "양의 정수만 입력해주세요.")
        @NotNull(message = "방 번호를 입력해주세요.")
        private Integer roomNumber;
        @NotBlank(message = "방 배정 여부를 입력해주세요.")
        private String assignedRoom;
        @NotNull(message = "입사일을 입력해주세요.")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd")
        private LocalDate admissionDate;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd")
        private LocalDate leavingDate;
        @NotNull(message = "학기 시작일을 입력해주세요.")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd")
        private LocalDate semesterStartDate;
        @NotNull(message = "학기 종료일을 입력해주세요.")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd")
        private LocalDate semesterEndDate;
        @PhoneNumber
        private String phoneNumber;
        @NotBlank(message = "사회코드를 입력해주세요.")
        private String socialCode;
        @NotBlank(message = "사회명을 입력해주세요.")
        private String socialName;
        @NotBlank(message = "우편번호를 입력해주세요.")
        private String zipCode;
        @NotBlank(message = "주소를 입력해주세요.")
        private String address;

        public Resident toEntity(){
            return Resident.builder()
                    .name(name)
                    .gender(gender)
                    .studentId(studentId)
                    .semester(semester)
                    .currentStatus(currentStatus)
                    .dateOfBirth(dateOfBirth)
                    .residenceSemester(residenceSemester)
                    .dormitory(dormitory)
                    .major(major)
                    .grade(grade)
                    .period(period)
                    .roomNumber(roomNumber)
                    .assignedRoom(assignedRoom)
                    .admissionDate(admissionDate)
                    .leavingDate(leavingDate)
                    .semesterStartDate(semesterStartDate)
                    .semesterEndDate(semesterEndDate)
                    .phoneNumber(phoneNumber)
                    .socialCode(socialCode)
                    .socialName(socialName)
                    .zipCode(zipCode)
                    .address(address)
                    .build();
        }
    }
}
