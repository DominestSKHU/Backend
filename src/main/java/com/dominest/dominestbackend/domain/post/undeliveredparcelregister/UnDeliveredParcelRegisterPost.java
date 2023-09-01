package com.dominest.dominestbackend.domain.post.undeliveredparcelregister;

import com.dominest.dominestbackend.domain.post.common.Post;
import com.dominest.dominestbackend.domain.post.component.category.Category;
import com.dominest.dominestbackend.domain.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class UnDeliveredParcelRegisterPost extends Post {
        // 이름과 연락처, 처리 내용 의 경우는 값을 따로 검증하지 않는다. 빈 값 혹은 입사생 DB에 없는 값이어도 괜찮다.
        // 길이가 너무 긴 경우에만 요청 거부하면 될 듯??
//        이름: ...,
//        연락처: ...,
//        처리 내용: ...,
//
//        // 처리 결과는 (문자발송, 전화완료, 폐기예정, 폐기완료 중 하나)
//        처리 결과: ...

    public UnDeliveredParcelRegisterPost(String currentDate, User writer, Category category) {
        // currentDate는 "yyyy-MM-dd 장기미수령 택배" 형식의 문자열이다.
        super(currentDate, writer, category);
//        // 현재 시간을 기준으로 LocalDateTime 객체 생성
//        LocalDateTime now = LocalDateTime.now();
//
//        // 원하는 형식의 문자열로 변환
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        String formattedDate = now.format(formatter);
//        String title = formattedDate + " 장기미수령 택배";
    }
}





