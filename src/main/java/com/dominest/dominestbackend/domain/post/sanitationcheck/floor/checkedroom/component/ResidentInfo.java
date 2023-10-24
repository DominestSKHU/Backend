package com.dominest.dominestbackend.domain.post.sanitationcheck.floor.checkedroom.component;

import com.dominest.dominestbackend.domain.resident.Resident;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

// 입사자의 이름, 학번, 전화번호를 담는 Embeddable 클래스
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Embeddable
public class ResidentInfo {
    String name;
    String studentId;
    String phoneNo;

    public static ResidentInfo from(Resident resident){
        return new ResidentInfo(resident.getName(), resident.getStudentId(), resident.getPhoneNumber());
    }
}
