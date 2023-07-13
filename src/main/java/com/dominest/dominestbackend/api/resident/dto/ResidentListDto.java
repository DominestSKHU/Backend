package com.dominest.dominestbackend.api.resident.dto;

import com.dominest.dominestbackend.domain.resident.Resident;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ResidentListDto {
    @Builder
    @Getter
    public static class Res{
        private List<ResidentDto> residents;

        public static Res from(List<Resident> residents){
            List<ResidentDto> residentDtos = residents.stream()
                    .map(ResidentDto::from)
                    .collect(Collectors.toList());
            return Res.builder().residents(residentDtos).build();
        }

        @Getter
        @Builder
        private static class ResidentDto{
            private Long id;
            private String name;
            private String gender;
            private String studentId;
            private String major;
            private String grade;
            private LocalDate dateOfBirth;
            private String semester;
            private String currentStatus;
            private String dormitory;
            private String period;
            private Integer roomNumber;
            private String assignedRoom;
            private LocalDate admissionDate;
            private LocalDate leavingDate;
            private LocalDate semesterStartDate;
            private LocalDate semesterEndDate;
            private String phoneNumber;
            private String socialCode;
            private String socialName;
            private String zipCode;
            private String address;

            //from
            public static ResidentDto from(Resident resident){
                return ResidentDto.builder()
                        .id(resident.getId())
                        .name(resident.getName())
                        .gender(resident.getGender())
                        .studentId(UUID.randomUUID().toString()) // todo unique 제한 때문에 임시로 UUID 사용.
                        .semester(resident.getSemester())
                        .currentStatus(resident.getCurrentStatus())
                        .dateOfBirth(resident.getDateOfBirth())
                        .dormitory(resident.getDormitory())
                        .major(resident.getMajor())
                        .grade(resident.getGrade())
                        .period(resident.getPeriod())
                        .roomNumber(resident.getRoomNumber())
                        .assignedRoom(resident.getAssignedRoom())
                        .admissionDate(resident.getAdmissionDate())
                        .leavingDate(resident.getLeavingDate())
                        .semesterStartDate(resident.getSemesterStartDate())
                        .semesterEndDate(resident.getSemesterEndDate())
                        .phoneNumber(resident.getPhoneNumber())
                        .socialCode(resident.getSocialCode())
                        .socialName(resident.getSocialName())
                        .zipCode(resident.getZipCode())
                        .address(resident.getAddress())
                        .build();
            }
        }
    }
}