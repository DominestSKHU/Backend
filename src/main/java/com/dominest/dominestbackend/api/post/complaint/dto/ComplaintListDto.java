package com.dominest.dominestbackend.api.post.complaint.dto;

import com.dominest.dominestbackend.api.common.AuditLog;
import com.dominest.dominestbackend.api.common.CategoryDto;
import com.dominest.dominestbackend.api.common.PageInfoDto;
import com.dominest.dominestbackend.domain.post.complaint.Complaint;
import com.dominest.dominestbackend.domain.post.component.category.Category;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ComplaintListDto {

    @Getter
    public static class Res {
        CategoryDto category;
        PageInfoDto page;

        List<ComplaintDto> complaints;

        public static Res from(Page<Complaint> complaintPage, Category category) {
            CategoryDto categoryDto = CategoryDto.from(category);
            PageInfoDto pageInfoDto = PageInfoDto.from(complaintPage);

            List<ComplaintDto> complaints = ComplaintDto.from(complaintPage);
            return new Res(categoryDto, pageInfoDto, complaints);
        }

        public Res(CategoryDto category, PageInfoDto page, List<ComplaintDto> complaints) {
            this.category = category;
            this.page = page;
            this.complaints = complaints;
        }

    }

    @Builder
    @Getter
    static class ComplaintDto {
        Long id;
        LocalDate date; // 민원접수일자
        String roomNo; // 호실
        String complaintCause; // 민원내역. FtIdx 만들어야 함.
        String complaintResolution; // 민원처리내역. FtIdx 만들어야 함.
        Complaint.ProcessState processState; // 처리상태

        AuditLog auditLog;

        static ComplaintDto from(Complaint complaint){
            return ComplaintDto.builder()
                    .id(complaint.getId())
                    .date(complaint.getDate())
                    .roomNo(complaint.getRoomNo())
                    .complaintCause(complaint.getComplaintCause())
                    .complaintResolution(complaint.getComplaintResolution())
                    .processState(complaint.getProcessState())
                    .auditLog(AuditLog.from(complaint))
                    .build();
        }

        static List<ComplaintDto> from(Page<Complaint> complaints){
            return complaints.stream()
                    .map(ComplaintDto::from)
                    .collect(Collectors.toList());
        }
    }
}
