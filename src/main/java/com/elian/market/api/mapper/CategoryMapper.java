package com.elian.market.api.mapper;

import com.elian.market.api.domain.Category;
import com.elian.market.api.dto.CategoryRequestDto;
import com.elian.market.api.dto.CategoryResponseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Category toEntity(CategoryRequestDto categoryRequestDto);

    CategoryResponseDto toResponseDto(Category category);
}
