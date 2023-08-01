package com.dominest.dominestbackend.api.resident.dto;

import com.dominest.dominestbackend.domain.resident.Resident;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

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
            String residentName;
            String existsFile;

            public PdfDto(Resident resident) {
                this.residentName = resident.getName();
                this.existsFile = resident.getPdfFileName() != null ? "성공" : "오류(파일없음)";
            }
        }
    }
}
