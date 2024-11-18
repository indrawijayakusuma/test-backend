package com.indra.test_backend.entity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class VendorCreateDto {
    @NotBlank(message = "Vendor name cannot be empty")
    @Size(min = 3, max = 20, message = "Vendor name must be between 3 and 20 characters")
    private String name;
}
