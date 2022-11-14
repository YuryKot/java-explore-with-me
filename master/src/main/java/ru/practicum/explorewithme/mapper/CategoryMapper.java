package ru.practicum.explorewithme.mapper;

import ru.practicum.explorewithme.dto.CategoryDto;
import ru.practicum.explorewithme.model.Category;

public class CategoryMapper {

    public static CategoryDto toCategoryDto(Category category) {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(categoryDto.getId());
        categoryDto.setName(categoryDto.getName());
        return categoryDto;
    }

    public static Category toCategory(CategoryDto categoryDto) {
        Category category = new Category();
        category.setId(categoryDto.getId());
        category.setName(categoryDto.getName());
        return category;
    }
}
