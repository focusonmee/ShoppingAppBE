package com.example.shopapp.controller;

import com.example.shopapp.component.LocalizationUtils;
import com.example.shopapp.dto.CategoryDTO;
import com.example.shopapp.model.Category;
import com.example.shopapp.response.UpdateCategoryResponse;
import com.example.shopapp.service.CategoryService;
import com.example.shopapp.service.ICategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.LocaleResolver;

import java.util.List;

//@Validated
@RestController
@RequestMapping("${api.prefix}/categories")
@RequiredArgsConstructor
public class CategoryController {
    //dependency injection
    private final ICategoryService categoryService;
    private final LocaleResolver localeResolver;
    private final MessageSource messageSource;
    private final LocalizationUtils localizationUtils;


    // them du lieu
    @PostMapping("")
    public ResponseEntity<?> createCategory(@Valid @RequestBody CategoryDTO categoryDTO,
                                            BindingResult result) {
        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(errorMessages);
        }

        // Giả sử categoryService.createCategory(categoryDTO) trả về đối tượng Category đã được lưu
        Category createdCategory = categoryService.createCategory(categoryDTO);
        return ResponseEntity.ok("Insert category successfully "+createdCategory); // Trả về đối tượng vừa được tạo
    }


    // Lay Du lieu
    @GetMapping("") //http://localhost:8080/api/v1/categories?page=1&limit=10
    public ResponseEntity<List<Category>> getAllCategories(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit
    ) {
        // Bạn có thể thay đổi phản hồi thành một đối tượng phức tạp hơn nếu cần
        List<Category>categories=categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }


    // update du lieu
    @PutMapping("/{id}")
    public ResponseEntity<UpdateCategoryResponse> updateCategory(
            @PathVariable Long id
            ,@Valid @RequestBody CategoryDTO categoryDTO) {
        categoryService.updateCategory(id,categoryDTO);
        return ResponseEntity.ok(UpdateCategoryResponse.builder()
                .message("category.update_category.update_successfully")
                .build());
    }

    //xoadulieu
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteCategory (@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok("Delete category with id =" + id +" successfully");
    }

}
