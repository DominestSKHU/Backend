package com.dominest.dominestbackend.domain.post.undeliveredparcel.component;

import com.dominest.dominestbackend.domain.common.BaseEntity;
import com.dominest.dominestbackend.domain.post.undeliveredparcel.UndeliveredParcelPost;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.*;

import javax.persistence.*;

/**
 * 장기미수령 택배 관리대장 게시글 내부의 관리물품 데이터
 * (이름, 연락처, 처리 내용)
 * 작성자와 외래키 관계는 맺을 필요가 없으므로 제외하기로 함.
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class UndeliveredParcel extends BaseEntity {

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
    @Enumerated(EnumType.STRING)
    private ProcessState processState;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "post_id")
    private UndeliveredParcelPost post;

    @Builder
    private UndeliveredParcel(String recipientName, String recipientPhoneNum, String instruction, ProcessState processState, UndeliveredParcelPost post) {
        this.recipientName = recipientName;
        this.recipientPhoneNum = recipientPhoneNum;
        this.instruction = instruction;
        this.processState = processState;
        this.post = post;
    }

    public void updateValues(String recipientName, String recipientPhoneNum, String instruction, ProcessState processState) {
        this.recipientName = recipientName;
        this.recipientPhoneNum = recipientPhoneNum;
        this.instruction = instruction;
        this.processState = processState;
    }

    // 처리 결과는 (문자발송, 전화완료, 폐기예정, 폐기완료 중 하나)
    @RequiredArgsConstructor
    public enum ProcessState {
        MESSAGE_SENT("문자발송"), CALL_COMPLETED("전화완료"),
        DISCARD_SCHEDULED("폐기예정"), DISCARD_COMPLETED("폐기완료");

        @JsonValue // 직렬화, 역직렬화 시 사용될 값
        private final String state;
    }

}















