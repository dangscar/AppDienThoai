package com.nlhd.data.mapper

import com.nlhd.data.model.manageCategory.Category
import com.nlhd.data.model.manageCategory.CategoryResponseDto
import com.nlhd.domain.entity.manageCategory.CategoryResponse

fun CategoryResponseDto.toDomain(categoryResponseDto: CategoryResponseDto): CategoryResponse {
    return CategoryResponse(
        categories = categoryResponseDto.categories.map { it.toDomain(it) },
        message = categoryResponseDto.message
    )
}

fun Category.toDomain(category: Category): com.nlhd.domain.entity.manageCategory.Category {
    return com.nlhd.domain.entity.manageCategory.Category(
        id = category.id,
        name = category.name
    )
}