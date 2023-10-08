package com.dominest.dominestbackend.api.resident.dto;

import com.dominest.dominestbackend.domain.resident.component.ResidenceSemester;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExcelUploadDto {
    @Getter
    @AllArgsConstructor
    public static class Req {
        @NotNull(message = "엑셀파일을 첨부해주세요.")
        MultipartFile file;
        @NotNull(message = "거주학기를 입력해주세요.")
        ResidenceSemester residenceSemester;
    }

    @Getter
    @AllArgsConstructor
    public static class Res {
        int originalRow;
        int successRow;

        public String getResultMsg() {
            return originalRow + "개의 행 중 " + successRow + "개의 행이 성공적으로 업로드되었습니다.";
        }
        public static Res of(int originalRow, int successRow) {
            return new Res(originalRow, successRow);
        }
    }
}



