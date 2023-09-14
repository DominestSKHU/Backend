package com.dominest.dominestbackend.api.category.controller;

import com.dominest.dominestbackend.api.category.request.CategoryUpdateRequest;
import com.dominest.dominestbackend.api.category.request.CreateCategoryRequest;
import com.dominest.dominestbackend.api.category.response.CategoryListDto;
import com.dominest.dominestbackend.api.category.response.CategoryListWithFavoriteDto;
import com.dominest.dominestbackend.api.common.RspTemplate;
import com.dominest.dominestbackend.domain.post.component.category.Category;
import com.dominest.dominestbackend.domain.post.component.category.repository.CategoryRepository;
import com.dominest.dominestbackend.domain.post.component.category.service.CategoryService;
import com.dominest.dominestbackend.global.exception.exceptions.BusinessException;
import com.dominest.dominestbackend.global.util.PrincipalUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping()
public class CategoryController {
    private final CategoryService categoryService;
    private final CategoryRepository categoryRepository;

    // 카테고리 관리페이지 목록 조회, orderKey ASC로 조회.
    // 페이지네이션 하지 않음.
    @GetMapping("/categories")
    public RspTemplate<CategoryListDto.Res> handleGetCategoryList() {
        Sort sort = Sort.by("orderKey");
        List<Category> categories = categoryRepository.findAll(sort);

        CategoryListDto.Res resDto = CategoryListDto.Res.from(categories);
        return new RspTemplate<>(HttpStatus.OK, "카테고리 조회 성공", resDto);
    }

    // 현재 로그인한 사용자를 기준으로 즐겨찾기 여부와 함께 카테고리 조회
    @GetMapping("/my-categories")
    public RspTemplate<CategoryListWithFavoriteDto.Res> handleGetMyCategoryList(Principal principal) {
        // 즐찾목록 다 조회해서 카테고리 ID들을 찾아낸다.
        // 찾아낸 카테고리 ID들과 전체 카테고리 목록 중 일치하는 것들은 즐겨찾기가 되어있는 것이다.
        List<Long> categoryIdsFromFavorites = categoryService.getIdAllByUserEmail(PrincipalUtil.toEmail(principal));

        Sort sort = Sort.by("orderKey");
        List<Category> categories = categoryRepository.findAll(sort);

        CategoryListWithFavoriteDto.Res resDto = CategoryListWithFavoriteDto.Res.from(categories, categoryIdsFromFavorites);
        return new RspTemplate<>(HttpStatus.OK, "카테고리 조회 성공", resDto);
    }

    @PostMapping ("/categories")// 카테고리 생성
    public ResponseEntity<RspTemplate<Void>> createCategory(
            @RequestBody @Valid final CreateCategoryRequest reqDto
    ) {
        Category category = categoryService.create(reqDto.getCategoryName(), reqDto.getCategoryType(), reqDto.getExplanation());

        RspTemplate<Void> rspTemplate = new RspTemplate<>(
                HttpStatus.CREATED
                , category.getId() + "번 " + category.getName() + " 카테고리 생성 성공"
        );
        return ResponseEntity
                .created(URI.create(category.getPostsLink()))
                .body(rspTemplate);
    }

    @PutMapping("/categories") // 카테고리 수정
    public RspTemplate<String> updateCategories(@RequestBody @Valid final CategoryUpdateRequest reqDto
    ) {
        int updateCount;
        try {
            updateCount = categoryService.update(reqDto);
        } catch (DataIntegrityViolationException e) {
            throw new BusinessException("카테고리 수정 실패, name 중복 혹은 값의 누락을 확인해주세요", HttpStatus.BAD_REQUEST);
        }

        return new RspTemplate<>(HttpStatus.OK
                , updateCount + "개의 카테고리 변경 요청 성공");
    }

    @DeleteMapping("/categories/{id}") // 카테고리 삭제
    public RspTemplate<String> deleteCategory(@PathVariable Long id) {
        categoryService.deleteById(id);
        return new RspTemplate<>(HttpStatus.OK, id +"번 카테고리 삭제 성공");
    }
}