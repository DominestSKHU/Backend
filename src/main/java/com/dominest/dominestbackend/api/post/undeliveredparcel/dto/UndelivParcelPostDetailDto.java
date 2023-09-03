package com.dominest.dominestbackend.api.post.undeliveredparcel.dto;

import com.dominest.dominestbackend.domain.post.undeliveredparcel.UndeliveredParcelPost;
import com.dominest.dominestbackend.domain.post.undeliveredparcel.component.UndeliveredParcel;
import com.dominest.dominestbackend.global.util.PrincipalUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UndelivParcelPostDetailDto {
    @Getter
    @AllArgsConstructor
    public static class Res {
        UndelivParcelPostDto postDetail;

        public static Res from(UndeliveredParcelPost post) {
            UndelivParcelPostDto postDto = UndelivParcelPostDto.from(post);
            return new Res(postDto);
        }

        @Getter
        @Builder
        static class UndelivParcelPostDto {
            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
            LocalDateTime createTime;
            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
            LocalDateTime lastModifiedTime;
            String title;
            String lastModifiedBy;
            List<UndelivParcelDto> undelivParcels;

            static UndelivParcelPostDto from(UndeliveredParcelPost post) {
                List<UndelivParcelDto> parcelDtos = UndelivParcelDto.from(post.getUndelivParcels());

                return UndelivParcelPostDto.builder()
                        .createTime(post.getCreateTime())
                        .lastModifiedTime(post.getLastModifiedTime())
                        .title(post.getTitle())
                        .lastModifiedBy(PrincipalUtil.strToName(post.getLastModifiedBy()))
                        .undelivParcels(parcelDtos)
                        .build();
            }
        }

        @Getter
        @Builder
        static class UndelivParcelDto {
            Long id;
            String recipientName;
            String recipientPhoneNum;
            String instruction;
            UndeliveredParcel.ProcessState processState;

            static UndelivParcelDto from(UndeliveredParcel undelivParcel) {
                return UndelivParcelDto.builder()
                        .id(undelivParcel.getId())
                        .recipientName(undelivParcel.getRecipientName())
                        .recipientPhoneNum(undelivParcel.getRecipientPhoneNum())
                        .instruction(undelivParcel.getInstruction())
                        .processState(undelivParcel.getProcessState())
                        .build();
            }
            static List<UndelivParcelDto> from(List<UndeliveredParcel> undelivParcels) {
                return undelivParcels.stream()
                        .map(UndelivParcelDto::from)
                        .collect(Collectors.toList());
            }
        }


    }
}
