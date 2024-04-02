package com.example.springbootecommerce.dto.productcategoryentity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryEntityUpdateDto {

    private int id;

    private String name;

    private String description;
}
