package com.dominest.dominestbackend.api.post.complaint.dto;

import com.dominest.dominestbackend.domain.post.complaint.Complaint;
import com.dominest.dominestbackend.domain.post.component.category.Category;
import com.dominest.dominestbackend.domain.user.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateComplaintDto {
    @NoArgsConstructor
    @Getter
    public static class Req {
        @NotBlank(message = "기숙사 방 번호를 입력해주세요.")
        private String roomNo; // N호실

        // 바인딩 실패 혹은 NULL 삽입 시 empty String으로 대체
        private String complaintCause = ""; // 민원내역. FtIdx 만들어야 함.
        // 바인딩 실패 혹은 NULL 삽입 시 empty String으로 대체
        private String complaintResolution = ""; // 민원처리내역. FtIdx 만들어야 함.

        @NotNull(message = "민원처리상태를 입력해주세요.")
        private Complaint.ProcessState processState;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        @NotNull(message = "민원접수일자를 입력해주세요.")
        private LocalDate date; // 민원접수일자

        public Complaint toEntity(User user, Category category) {
            return Complaint.builder()
                    .roomNo(roomNo)
                    .complaintCause(complaintCause)
                    .complaintResolution(complaintResolution)
                    .processState(processState)
                    .date(date)
                    .writer(user)
                    .category(category)
                    .build();
        }
    }
}
