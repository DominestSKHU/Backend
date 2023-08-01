package com.dominest.dominestbackend.api.resident.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

public class PdfBulkUploadDto {

    @Getter
    @NoArgsConstructor
    public static class Res{
        List<UploadDto> files = new ArrayList<>();
        int successCount = 0;

        public void addSuccessCount(){
            successCount++;
        }

        public void addToDtoList(String filename, String status, String failReason){
            UploadDto uploadDto = UploadDto.builder()
                    .filename(filename)
                    .status(status)
                    .failReason(failReason)
                    .build();
            files.add(uploadDto);
        }

        @Getter
        @Builder
        static class UploadDto{
            String filename;
            String status;
            String failReason;
        }
    }
}




















