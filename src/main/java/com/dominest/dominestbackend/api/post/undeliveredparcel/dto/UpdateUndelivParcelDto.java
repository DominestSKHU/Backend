package com.dominest.dominestbackend.api.post.undeliveredparcel.dto;

import com.dominest.dominestbackend.domain.post.undeliveredparcel.component.UndeliveredParcel;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)

public class UpdateUndelivParcelDto {
    @NoArgsConstructor
    @Getter
    public static class Req {
        @Length(max = 50, message = "수취인 이름은 50자를 넘을 수 없습니다")
        String recipientName;
        @Length(max = 50, message = "수취인 전화번호는 50자를 넘을 수 없습니다")
        String recipientPhoneNum;
        @Length(max = 500, message = "처리내용 설명은 500자를 넘을 수 없습니다")
        String instruction;
        @NotNull(message = "처리상태는 비어있을 수 없습니다")
        UndeliveredParcel.ProcessState processState;
    }
}











