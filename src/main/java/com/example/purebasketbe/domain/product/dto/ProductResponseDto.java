package com.example.purebasketbe.domain.product.dto;

import com.example.purebasketbe.domain.product.entity.Product;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductResponseDto extends ProductResponse {

    private List<String> imgUrlList;

    private ProductResponseDto(Product product, List<String> imgUrlList) {
        ProductResponse.from(product);
        this.imgUrlList = imgUrlList;
    }

    public static ProductResponseDto of(Product product, List<String> imgUrlList) {
        return new ProductResponseDto(product, imgUrlList);
    }

}
