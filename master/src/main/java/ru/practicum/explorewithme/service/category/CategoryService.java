package ru.practicum.explorewithme.service.category;

import ru.practicum.explorewithme.dto.CategoryDto;

import java.util.List;

public interface CategoryService {
    List<CategoryDto> getCategories(Integer from, Integer size);

    CategoryDto getCategory(Long catId);

    CategoryDto updateCategory(CategoryDto categoryDto);

    CategoryDto addCategory(CategoryDto categoryDto);

    void deleteCategory(Long catId);
}
