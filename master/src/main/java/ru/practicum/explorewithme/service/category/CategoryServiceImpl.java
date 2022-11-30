package ru.practicum.explorewithme.service.category;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.dao.CategoryRepository;
import ru.practicum.explorewithme.dto.category.CategoryDto;
import ru.practicum.explorewithme.dto.category.NewCategoryDto;
import ru.practicum.explorewithme.exception.CategoryNotFoundException;
import ru.practicum.explorewithme.mapper.CategoryMapper;
import ru.practicum.explorewithme.model.Category;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<CategoryDto> getCategories(Integer from, Integer size) {
        int page = from / size;
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return categoryRepository.findAll(pageable)
                .stream()
                .map(CategoryMapper::toCategoryDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto getCategory(Long catId) {
        return CategoryMapper.toCategoryDto(findCategory(catId));
    }

    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto) {
        Category category = findCategory(categoryDto.getId());
        category.setName(categoryDto.getName());
        categoryRepository.save(category);
        return CategoryMapper.toCategoryDto(category);
    }

    @Override
    public CategoryDto addCategory(NewCategoryDto newCategoryDto) {
        Category category = CategoryMapper.toCategory(newCategoryDto);
        categoryRepository.save(category);
        return CategoryMapper.toCategoryDto(category);
    }

    @Override
    public void deleteCategory(Long catId) {
        findCategory(catId);
        categoryRepository.deleteById(catId);
    }

    private Category findCategory(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));
    }
}
