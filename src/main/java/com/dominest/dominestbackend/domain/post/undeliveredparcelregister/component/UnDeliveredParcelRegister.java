package com.dominest.dominestbackend.domain.post.undeliveredparcelregister.component;

import com.dominest.dominestbackend.domain.common.BaseEntity;
import com.dominest.dominestbackend.domain.post.undeliveredparcelregister.UnDeliveredParcelRegisterPost;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.*;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class UnDeliveredParcelRegister extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 이름과 연락처, 처리 내용(사실상 안내내용) 의 경우는 값을 따로 검증하지 않는다. 빈 값 혹은 입사생 DB에 없는 값이어도 괜찮다.
    // 길이가 너무 긴 경우에만 요청 거부하면 될 듯??
    @Column(nullable = false)
    private String recipientName;
    @Column(nullable = false)
    private String recipientPhoneNum;
    @Column(nullable = false)
    private String instruction;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "post_id")
    private UnDeliveredParcelRegisterPost post;

    @Builder
    private UnDeliveredParcelRegister(String recipientName, String recipientPhoneNum, String instruction, UnDeliveredParcelRegisterPost post) {
        this.recipientName = recipientName;
        this.recipientPhoneNum = recipientPhoneNum;
        this.instruction = instruction;
        this.post = post;
    }

    // 처리 결과는 (문자발송, 전화완료, 폐기예정, 폐기완료 중 하나)
    @RequiredArgsConstructor
    enum ProcessState {
        MESSAGE_SENT("문자발송"), CALL_COMPLETED("전화완료"),
        DISCARD_SCHEDULED("폐기예정"), DISCARD_COMPLETED("폐기완료");

        @JsonValue // 직렬화 시 사용될 값
        private final String state;
    }

}















