package com.example.springbootecommerce.dto.supplierentity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SupplierEntityUpdateDto {

    private int id;

    private String name;

    private String telephone;

    private String address1;

    private String address2;
}
