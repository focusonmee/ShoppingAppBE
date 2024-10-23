package com.example.shopapp.service;

import com.example.shopapp.dto.CategoryDTO;
import com.example.shopapp.model.Category;
import com.example.shopapp.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CategoryService implements ICategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public Category createCategory(CategoryDTO categoryDTO) {
        Category newCategory = Category
                .builder()
                .name(categoryDTO.getName())
                .build();
        return categoryRepository.save(newCategory);
    }

    @Override
    public Category getCategoryById(long id) {
        // Tìm Category theo ID, nếu không tìm thấy thì ném Exception
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }

    @Override
    public List<Category> getAllCategories() {
        // Lấy tất cả Category từ DB
        return categoryRepository.findAll();
    }

    @Override
    public Category updateCategory(long categoryId
            ,    CategoryDTO categoryDTO) {
        // Tìm Category cần update
        Category existingCategory = getCategoryById(categoryId);

        // Cập nhật thông tin Category
        existingCategory.setName(categoryDTO.getName());

        // Lưu lại thay đổi vào DB
        return categoryRepository.save(existingCategory);
    }


    @Override
    public void deleteCategory(long id) {
        // Xóa Category theo ID
        categoryRepository.deleteById(id);
    }
}
