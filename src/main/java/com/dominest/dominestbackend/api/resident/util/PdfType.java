package com.dominest.dominestbackend.api.resident.util;

import com.dominest.dominestbackend.domain.resident.Resident;
import com.dominest.dominestbackend.global.util.FileService;

public enum PdfType {
    ADMISSION, DEPARTURE // 입사신청서, 퇴사신청서
    ;
    public static PdfType from(String pdfType){
        return PdfType.valueOf(pdfType.toUpperCase());
    }
    public FileService.FilePrefix toFilePrefix(){
        if (this.equals(ADMISSION)){
            return FileService.FilePrefix.RESIDENT_ADMISSION;
        }
        return FileService.FilePrefix.RESIDENT_DEPARTURE;
    }

    public String getPdfFileName(Resident resident){
        if (this.equals(ADMISSION)){
            return resident.getAdmissionPdfFileName();
        }
        return resident.getDeparturePdfFileName();
    }
}
