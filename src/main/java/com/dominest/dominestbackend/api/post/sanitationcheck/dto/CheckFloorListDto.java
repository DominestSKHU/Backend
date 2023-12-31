package com.dominest.dominestbackend.api.post.sanitationcheck.dto;


import com.dominest.dominestbackend.api.common.AuditLog;
import com.dominest.dominestbackend.api.common.CategoryDto;
import com.dominest.dominestbackend.domain.post.component.category.Category;
import com.dominest.dominestbackend.domain.post.sanitationcheck.floor.Floor;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

// 지정한 게시글을 클릭하면 층 목록이 반환되는데, 이때의 층 목록을 반환하는 DTO
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CheckFloorListDto {
    @Getter
    public static class Res {
        CategoryDto category;
        List<CheckFloorDto> posts;
        public static Res from(List<Floor> floors, Category category){
            CategoryDto categoryDto = CategoryDto.from(category);

            List<CheckFloorDto> posts
                    = CheckFloorDto.from(floors);

            return new Res(posts, categoryDto);
        }

        Res(List<CheckFloorDto> posts, CategoryDto category) {
            this.posts = posts;
            this.category = category;
        }

        @Builder
        @Getter
        static class CheckFloorDto {
            long id;
            String floor;
            AuditLog auditLog;

            static CheckFloorDto from(Floor floor, int floorNum){
                return CheckFloorDto.builder()
                        .id(floor.getId())
                        .floor(String.format("%d층", floorNum))
                        .auditLog(AuditLog.from(floor))
                        .build();
            }

            static List<CheckFloorDto> from(List<Floor> floors){
                List<CheckFloorDto> floorDtos = new ArrayList<>();
                int floorNum = 2;
                for (Floor floor : floors) {
                    floorDtos.add(CheckFloorDto.from(floor, floorNum++));
                }
                return floorDtos;
            }
        }
    }
}
