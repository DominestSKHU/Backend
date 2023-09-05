package com.dominest.dominestbackend.api.resident.dto;

import com.dominest.dominestbackend.domain.resident.Resident;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ResidentPdfListDto {
    @Getter
    public static class Res {
        private List<PdfDto> pdfs;

        public static Res from(List<Resident> residents) {
            List<PdfDto> pdfDtos = residents.stream()
                    .map(PdfDto::new)
                    .collect(Collectors.toList());
            return new Res(pdfDtos);
        }

        private Res(List<PdfDto> pdfs) {
            this.pdfs = pdfs;
        }

        @Getter
        private static class PdfDto {
            // 사용자 화면에 이름, 파일존재유무, 개별파일 조회 url
            long id;
            String residentName;
            String existsAdmissionFile;
            String existsDepartureFile;

            public PdfDto(Resident resident) {
                this.id = resident.getId();
                this.residentName = resident.getName();
                this.existsAdmissionFile = resident.getAdmissionPdfFileName() != null ? "성공" : "오류(파일없음)";
                this.existsDepartureFile = resident.getDeparturePdfFileName() != null ? "성공" : "오류(파일없음)";
            }
        }
    }
}
