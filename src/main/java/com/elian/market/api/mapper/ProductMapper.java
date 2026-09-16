package com.elian.market.api.mapper;

import com.elian.market.api.domain.Product;
import com.elian.market.api.dto.ProductRequestDto;
import com.elian.market.api.dto.ProductResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "category", ignore = true)
    Product toEntity(ProductRequestDto productRequestDto);

    @Mapping(target = "categoryName", source = "category.name")
    ProductResponseDto toResponseDto(Product product);
}
